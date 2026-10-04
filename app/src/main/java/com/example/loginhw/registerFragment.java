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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.loginhw.databinding.FragmentRegisterBinding;

public class registerFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    public registerFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupTextWatchers();
        setupLoginSpan();
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
                    binding.tilEmail.setError(null);
                    binding.tilUsername.setError(null);
                    binding.tilPassword.setError(null);
                    binding.tilConfirmPassword.setError(null);
                    validateInputs();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etUsername.addTextChangedListener(textWatcher);
        binding.etPassword.addTextChangedListener(textWatcher);
        binding.etConfirmPassword.addTextChangedListener(textWatcher);
    }

    private void validateInputs() {
        if (binding == null) return;

        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

        boolean isEmailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isUsernameValid = !username.isEmpty();
        boolean isPasswordValid = !password.isEmpty() && password.length() >= 6;
        boolean isConfirmValid = !confirmPassword.isEmpty() && confirmPassword.equals(password);

        if (isEmailValid && isUsernameValid && isPasswordValid && isConfirmValid) {
            int primaryColor = ContextCompat.getColor(requireContext(), R.color.primary);
            int darkTextColor = ContextCompat.getColor(requireContext(), R.color.background);
            binding.btnRegister.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
            binding.btnRegister.setTextColor(darkTextColor);
        } else {
            int disabledColor = ContextCompat.getColor(requireContext(), R.color.button_disabled);
            int disabledTextColor = ContextCompat.getColor(requireContext(), R.color.button_disabled_text);
            binding.btnRegister.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            binding.btnRegister.setTextColor(disabledTextColor);
        }
    }

    private void setupLoginSpan() {
        String text = "Log in";
        SpannableString spannableString = new SpannableString(text);

        int primaryColor = ContextCompat.getColor(requireContext(), R.color.primary);

        spannableString.setSpan(new ForegroundColorSpan(primaryColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvLogin.setText(spannableString);
        binding.tvLogin.setOnClickListener(v -> {
            hideKeyboard();
            NavHostFragment.findNavController(registerFragment.this).navigateUp();
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

        binding.tilConfirmPassword.setEndIconOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                binding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.tilConfirmPassword.setEndIconDrawable(R.drawable.eye_24);
            } else {
                binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.tilConfirmPassword.setEndIconDrawable(R.drawable.eye_slash_24);
            }
            if (binding.etConfirmPassword.getText() != null) {
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().length());
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            hideKeyboard();
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
            String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

            boolean isEmailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
            boolean isUsernameValid = !username.isEmpty();
            boolean isPasswordValid = !password.isEmpty() && password.length() >= 6;
            boolean isConfirmValid = !confirmPassword.isEmpty() && confirmPassword.equals(password);

            if (isEmailValid && isUsernameValid && isPasswordValid && isConfirmValid) {
                Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
            } else {
                if (!isEmailValid) binding.tilEmail.setError("Enter a valid email");
                if (!isUsernameValid) binding.tilUsername.setError("Enter a username");
                if (!isPasswordValid) binding.tilPassword.setError("Password must be at least 6 characters");
                if (!isConfirmValid) binding.tilConfirmPassword.setError("Passwords do not match");
                Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            }
        });

        View.OnClickListener socialClickListener = v -> {
            hideKeyboard();
            Toast.makeText(requireContext(), "Social Registration clicked", Toast.LENGTH_SHORT).show();
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