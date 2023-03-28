package com.ekachandra.githubuserapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekachandra.githubuserapp.R
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.databinding.FragmentFollowBinding
import com.ekachandra.githubuserapp.ui.adapters.UsersAdapter
import com.ekachandra.githubuserapp.ui.main.MainViewModel


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = arguments?.getInt(ARG_POSITION, 0)
        var username = arguments?.getString(ARG_USERNAME)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        mainViewModel.apply {
            isLoadingFollow.observe(requireActivity()) { loading ->
                showLoading(loading)
            }

            isErrorFollow.observe(requireActivity()) {
                if (it) {
                    Toast.makeText(
                        requireContext(), getString(R.string.api_failed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        if (position == 1) {
            if (savedInstanceState == null) {
                mainViewModel.getFollowers(username.toString())
            }

            mainViewModel.followers.observe(viewLifecycleOwner) {
                val listFollowers = ArrayList<User>()

                for (user in it) {
                    listFollowers.add(User(user.login, user.avatarUrl))
                }

                setUserFollowList(listFollowers)
            }
        } else {
            if (savedInstanceState == null) {
                mainViewModel.getFollowing(username.toString())
            }

            mainViewModel.following.observe(viewLifecycleOwner) {
                val listFollowing = ArrayList<User>()

                for (user in it) {
                    listFollowing.add(User(user.login, user.avatarUrl))
                }

                setUserFollowList(listFollowing)
            }
        }

    }

    private fun setUserFollowList(userFollowList: ArrayList<User>) {
        binding.rvUsersFollow.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = UsersAdapter(userFollowList)
        binding.rvUsersFollow.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.rvUsersFollow.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onPause() {
        super.onPause()
        binding.rvUsersFollow.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "section_username"
    }

}