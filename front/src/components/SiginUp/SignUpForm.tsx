import { Alert, AlertColor, AlertTitle, Box, Button, Snackbar, styled } from "@mui/material";
import { useState } from "react";
import FormInput from "../Common/FormInput";
import SignUpFormSubmitButton from "./SignUpFormSubmitButton";
import { SignUpRequest, signUpApi } from "../../services/SignUp/SignUpApi";

type AlertContent = {
    severity: AlertColor | undefined;
    title: string;
    content: string;
};

const SignUpFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    component: "form",
});

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const SignUpForm = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [nickname, setNickname] = useState<string>("");

    const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    const SignUpFormSubmitFunction = () => {
        const signUpRequest: SignUpRequest = {
            email: email,
            password: password,
            nickname: nickname,
        };

        signUpApi(signUpRequest)
            .then(() => {
                setAlertContent({
                    severity: "success",
                    title: "서버 가입 성공!",
                    content: "서버 가입에 성공했습니다.",
                });
            })
            .catch(() => {
                setAlertContent({
                    severity: "error",
                    title: "서버 가입 실패!",
                    content: "서버 가입에 실패했습니다.",
                });
            })
            .finally(() => {
                setOpenSnackbar(true);
            });
    };

    const signUpFormInputProps = [
        { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
        { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
        { type: "nickname", label: "NICKNAME", helperText: "닉네임을 입력하세요.", onFormData: setNickname },
    ];

    return (
        <SignUpFormBox
            component={"form"}
            onSubmit={(e) => {
                e.preventDefault();
                SignUpFormSubmitFunction();
            }}
        >
            {signUpFormInputProps.map((signUpFormInputProp) => (
                <FormInput
                    key={signUpFormInputProp.label}
                    type={signUpFormInputProp.type}
                    label={signUpFormInputProp.label}
                    helperText={signUpFormInputProp.helperText}
                    onFormData={signUpFormInputProp.onFormData}
                ></FormInput>
            ))}
            <SignUpFormSubmitButton></SignUpFormSubmitButton>

            <Snackbar
                open={openSnackbar}
                onClose={() => setOpenSnackbar(false)}
                anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
            >
                <Alert severity={alertContent.severity} action={alertButton(() => setOpenSnackbar(false))}>
                    <AlertTitle>{alertContent.title}</AlertTitle>
                    {alertContent.content}
                </Alert>
            </Snackbar>
        </SignUpFormBox>
    );
};

export default SignUpForm;
