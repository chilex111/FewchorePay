package fewchore.com.exolve.fewchore.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.views.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.fragment_profile, container, false)
        val name = view.findViewById<TextView>(R.id.textName)
        val email = view.findViewById<TextView>(R.id.textEmail)
        val phone  = view.findViewById<TextView>(R.id.textViewPhone)
        val profile = view.findViewById<CircleImageView>(R.id.imagePhoto)

        val nameText = AppUtils.getMyFirstName(activity)+" "+ AppUtils.getMyLastName(activity)
        val emailText = AppUtils.getMyEmail(activity)
        val phoneText = AppUtils.getMyPhoneNumber(activity)
        val profileImg = AppUtils.getMyProfile(activity)
        Picasso.with(activity).load(profileImg).into(profile)


        name.text = nameText
        email.text = emailText
        phone.text = phoneText

        view.findViewById<TextView>(R.id.textCard).setOnClickListener {
            childFragmentManager.beginTransaction()
                    .replace(R.id.container, CardListFragment.newInstance("2", null, "0"))
                    .commit()
        }
        view.findViewById<TextView>(R.id.textBank).setOnClickListener {
            childFragmentManager.beginTransaction()
                    .replace(R.id.container, BankListFragment.newInstance("2", null))
                    .commit()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
