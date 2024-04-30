import { Alert, AlertColor, AlertTitle, Box, Button, Snackbar, styled } from "@mui/material";
import { useState } from "react";
import FormInput from "../Common/FormInput";
import SignInFormSubmitButton from "./SignInFormSubmitButton";
import { SignInRequest, signInApi } from "../../services/Login/LoginApi";

type AlertContent = {
    severity: AlertColor | undefined;
    title: string;
    content: string;
};

const SignInFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    component: "form",
});

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const SignInForm = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [nickname, setNickname] = useState<string>("");

    const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    const signInFormSubmitFunction = () => {
        const signInRequest: SignInRequest = {
            email: email,
            password: password,
            nickname: nickname,
        };

        signInApi(signInRequest)
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

    const singInFormInputProps = [
        { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
        { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
        { type: "nickname", label: "NICKNAME", helperText: "닉네임을 입력하세요.", onFormData: setNickname },
    ];

    return (
        <SignInFormBox
            component={"form"}
            onSubmit={(e) => {
                e.preventDefault();
                signInFormSubmitFunction();
            }}
        >
            {singInFormInputProps.map((signFormInputProp) => (
                <FormInput
                    key={signFormInputProp.label}
                    type={signFormInputProp.type}
                    label={signFormInputProp.label}
                    helperText={signFormInputProp.helperText}
                    onFormData={signFormInputProp.onFormData}
                ></FormInput>
            ))}
            <SignInFormSubmitButton></SignInFormSubmitButton>

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
        </SignInFormBox>
    );
};

export default SignInForm;
