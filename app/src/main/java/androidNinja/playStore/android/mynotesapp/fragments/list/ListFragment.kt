package androidNinja.playStore.android.mynotesapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidNinja.playStore.android.mynotesapp.R
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidNinja.playStore.android.mynotesapp.data.viewmodel.NotesViewModel
import androidNinja.playStore.android.mynotesapp.fragments.SharedViewModel
import androidNinja.playStore.android.mynotesapp.fragments.list.adapter.ListAdapter
import androidNinja.playStore.android.mynotesapp.utils.hideKeyboard
import androidNinja.playStore.android.mynotesapp.utils.observeOnce
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mNotesViewModel: NotesViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        setHasOptionsMenu(true)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)


        mNotesViewModel.getAllNotes.observe(viewLifecycleOwner,  { data ->
            mSharedViewModel.checkIfDatabaseIsEmpty(data)
            adapter.setData(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner,  {
            showEmptyDatabase(it)
        })

        hideKeyboard(requireActivity())

        return view
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mNotesViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreDeletedData(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: NotesEntity){
        val snackbar = Snackbar.make(
            view,
            "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            mNotesViewModel.insertData(deletedItem)
        }
        snackbar.show()
    }

    private fun showEmptyDatabase(emptyDatabase: Boolean) {
        if(emptyDatabase){
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.VISIBLE
        }else{
            view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all ->{
                confirmRemoveAll()
            }
            R.id.menu_priority_high ->{
                mNotesViewModel.sortByHighPriority.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                })
            }
            R.id.menu_priority_low ->{
                mNotesViewModel.sortByLowPriority.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery= "%$query%"

        mNotesViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner, { list ->
            list?.let{
                adapter.setData(it)
            }
        })
    }

    private fun confirmRemoveAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mNotesViewModel.deleteAllData()
            Toast.makeText(requireContext(), "Successfully Deleted Everything", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }
}