package com.example.loginhw;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.loginhw.databinding.FragmentLoginfragmentBinding;

public class Loginfragment extends Fragment {

    private FragmentLoginfragmentBinding binding;
    private boolean isPasswordVisible = false;

    public Loginfragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginfragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupTextWatchers();
        setupSignUpSpan();
        setupListeners();
        validateInputs();
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding != null) {
                    binding.tilUsername.setError(null);
                    binding.tilPassword.setError(null);
                    validateInputs();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        binding.etUsername.addTextChangedListener(textWatcher);
        binding.etPassword.addTextChangedListener(textWatcher);
    }

    private void validateInputs() {
        if (binding == null) return;

        String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        boolean isUsernameValid = username.equalsIgnoreCase("admin")
                || username.equalsIgnoreCase("admin@email.com")
                || username.equalsIgnoreCase("johndoe@email.com");
        boolean isPasswordValid = password.equals("123456");

        if (isUsernameValid && isPasswordValid) {
            int primaryColor = ContextCompat.getColor(requireContext(), R.color.primary);
            int darkTextColor = ContextCompat.getColor(requireContext(), R.color.background);
            binding.btnLogin.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
            binding.btnLogin.setTextColor(darkTextColor);
        } else {
            int disabledColor = ContextCompat.getColor(requireContext(), R.color.button_disabled);
            int disabledTextColor = ContextCompat.getColor(requireContext(), R.color.button_disabled_text);
            binding.btnLogin.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            binding.btnLogin.setTextColor(disabledTextColor);
        }
    }

    private void setupSignUpSpan() {
        String text = "Sign Up";
        SpannableString spannableString = new SpannableString(text);

        int primaryColor = ContextCompat.getColor(requireContext(), R.color.primary);

        spannableString.setSpan(new ForegroundColorSpan(primaryColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvSignUp.setText(spannableString);
        binding.tvSignUp.setOnClickListener(v -> {
            hideKeyboard();
            NavHostFragment.findNavController(Loginfragment.this)
                    .navigate(R.id.action_loginfragment_to_registerFragment);
        });
    }

    private void setupListeners() {
        binding.getRoot().setOnClickListener(v -> hideKeyboard());

        binding.tilPassword.setEndIconOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;

            if (isPasswordVisible) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.tilPassword.setEndIconDrawable(R.drawable.eye_24);
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.tilPassword.setEndIconDrawable(R.drawable.eye_slash_24);
            }

            if (binding.etPassword.getText() != null) {
                binding.etPassword.setSelection(binding.etPassword.getText().length());
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            hideKeyboard();
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            boolean isUsernameValid = username.equalsIgnoreCase("admin")
                    || username.equalsIgnoreCase("admin@email.com")
                    || username.equalsIgnoreCase("johndoe@email.com");
            boolean isPasswordValid = password.equals("123456");

            if (isUsernameValid && isPasswordValid) {
                Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                if (!isUsernameValid) {
                    binding.tilUsername.setError("Invalid Username/Email (Use 'admin' or 'johndoe@email.com')");
                }
                if (!isPasswordValid) {
                    binding.tilPassword.setError("Invalid Password (Use '123456')");
                }
                Toast.makeText(requireContext(), "Please enter correct credentials", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            hideKeyboard();
            Toast.makeText(requireContext(), "Forgot Password clicked", Toast.LENGTH_SHORT).show();
        });

        View.OnClickListener socialClickListener = v -> {
            hideKeyboard();
            Toast.makeText(requireContext(), "Social Login clicked", Toast.LENGTH_SHORT).show();
        };

        binding.btnGoogle.setOnClickListener(socialClickListener);
        binding.btnFacebook.setOnClickListener(socialClickListener);
        binding.btnGithub.setOnClickListener(socialClickListener);
    }

    private void hideKeyboard() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideKeyboard();
        } else if (getContext() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getView();
            if (imm != null && view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}