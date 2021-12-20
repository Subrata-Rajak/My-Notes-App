package androidNinja.playStore.android.mynotesapp.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidNinja.playStore.android.mynotesapp.R
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidNinja.playStore.android.mynotesapp.data.viewmodel.NotesViewModel
import androidNinja.playStore.android.mynotesapp.fragments.SharedViewModel
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class AddFragment : Fragment() {

    private val notesViewModel: NotesViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private lateinit var mTitle: EditText
    private lateinit var mDescription: EditText
    private lateinit var mPriority: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        mTitle = view.findViewById(R.id.title_et)
        mDescription = view.findViewById(R.id.description_et)
        mPriority = view.findViewById(R.id.priorities_spinner)

        mPriority.onItemSelectedListener = mSharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title =mTitle.text.toString()
        val desc =mDescription.text.toString()
        val priority =mPriority.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, desc)

        if(validation){
            val newData = NotesEntity(
                0,
                title,
                mSharedViewModel.parsePriority(priority),
                desc
            )
            notesViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }
}