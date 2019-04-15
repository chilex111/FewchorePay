package fewchore.com.exolve.fewchore.child_fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.FragmentListener
import fewchore.com.exolve.fewchore.model.PasswordModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val LOAN_DETAILS = "param2"


class PasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var formModel: PasswordModel? = null
    private var listener: FragmentListener? = null

    private var editPasswrd: EditText? = null
    private var buttonSubmit : Button ?= null
    private var msg: TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            formModel = it.getParcelable(LOAN_DETAILS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_password, container, false)
        editPasswrd = view.findViewById(R.id.editPassword)as EditText

        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        msg = view.findViewById(R.id.msg)
        if (param1 == "invest"){
            msg!!.text = getString(R.string.invest__msg)
            buttonSubmit!!.text = getString(R.string.invest_now)
        }

        buttonSubmit!!.setOnClickListener{
           if (submitForm())
               listener!!.onFragmentNavigation(NavigationDirection.PASSWORD_FORWARD)
        }

        return view
    }

    private fun submitForm():Boolean {
        val passwordText = editPasswrd!!.text.toString()
        val savedPassword = AppUtils.getMyPassword(this.activity!!)
        Log.i("PASSWORD", savedPassword)
        if (passwordText.isEmpty()){
            editPasswrd!!.error = "This field is required"
            return false
        }
        if (savedPassword != passwordText){
            editPasswrd!!.error = "This password is incorrect.Try again"
            return false
        }
        else{
            formModel = PasswordModel()
            formModel!!.password = passwordText
            listener!!.onPasswordDetailSubmit(formModel!!)

            return true
        }
 }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } else {
            throw RuntimeException(context.toString() + " must implement Loan")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param loanFormModel Parameter 2.
         * @return A new instance of fragment PasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, loanFormModel: PasswordModel?) =
                PasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(LOAN_DETAILS, loanFormModel)
                    }
                }
    }
}
