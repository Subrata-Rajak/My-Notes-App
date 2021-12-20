package androidNinja.playStore.android.mynotesapp.fragments.update

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs

class UpdateFragment : Fragment() {

    private lateinit var current_title: EditText
    private lateinit var current_desCription: EditText
    private lateinit var current_priority_spinner: Spinner

    private val args by navArgs<UpdateFragmentArgs>()
    private val mShareViewModel: SharedViewModel by viewModels()
    private val mNotesViewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)

        current_title = view.findViewById(R.id.current_title_et)
        current_desCription = view.findViewById(R.id.current_description_et)
        current_priority_spinner = view.findViewById(R.id.current_priorities_spinner)

        current_title.setText(args.currentItem.title)
        current_desCription.setText(args.currentItem.description)
        current_priority_spinner.setSelection(mShareViewModel.parePriorityToInt(args.currentItem.priority))
        current_priority_spinner.onItemSelectedListener = mShareViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save ->{
                updateItem()
            }
            R.id.menu_delete ->{
                confirmItemRemoval()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mNotesViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(), "Successfully Deleted: ${args.currentItem.title}", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to delete '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateItem() {
        val title = current_title.text.toString()
        val description = current_desCription.text.toString()
        val priority = current_priority_spinner.selectedItem.toString()

        val validation = mShareViewModel.verifyDataFromUser(title, description)

        if(validation){
            val upDateItem = NotesEntity(
                args.currentItem.id,
                title,
                mShareViewModel.parsePriority(priority),
                description
            )
            mNotesViewModel.updateData(upDateItem)
            Toast.makeText(requireContext(),"Successfully updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }
}