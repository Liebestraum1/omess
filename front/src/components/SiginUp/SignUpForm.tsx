import { Alert, AlertTitle, Box, Button, Snackbar, styled } from "@mui/material";
import { useState } from "react";
import FormInput from "../Common/FormInput";
import SignUpFormSubmitButton from "./SignUpFormSubmitButton";
import { SignUpRequest, emailValidationApi, nicknameValidationApi, signUpApi } from "../../services/SignUp/SignUpApi";
import useFormListener from "../../hooks/FormListener";
import { AlertContent } from "../../types/common/Alert";

const SignUpFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    component: "form",
    width: "60%",
});

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const SignUpForm = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [nickname, setNickname] = useState<string>("");

    const [emailHelperText, setEmailHelperText] = useState<string>("");
    const [passwordHelperText, setPasswordHelperText] = useState<string>("");
    const [nicknameHelperText, setNicknameHelperText] = useState<string>("");

    const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+(\.[^\s@]+)*$/;

    useFormListener(
        email,
        () => {
            if (regex.test(email)) {
                emailValidationApi(email)
                    .then((response: any) => {
                        if (response["isExist"]) {
                            setEmailHelperText("이미 존재하는 이메일 주소입니다.");
                        } else {
                            setEmailHelperText("사용할 수 있는 이메일 주소입니다.");
                        }
                    })
                    .catch(() => {
                        setEmailHelperText("올바른 이메일 주소를 입력하세요.");
                    });
            } else {
                setEmailHelperText("이메일 주소를 입력하세요.");
            }
        },
        () => setEmailHelperText("이메일 주소를 입력하세요."),
        200
    );

    useFormListener(
        password,
        () => {
            if (password.length < 8 || password.length > 20) {
                setPasswordHelperText("비밀번호를 8자 이상, 20자 이하로 입력해 주세요.");
            } else {
                setPasswordHelperText("올바른 비밀번호입니다.");
            }
        },
        () => setPasswordHelperText("비밀번호를 입력하세요."),
        200
    );

    useFormListener(
        nickname,
        () => {
            nicknameValidationApi(nickname)
                .then((response: any) => {
                    if (response["isExist"]) {
                        setNicknameHelperText("이미 존재하는 닉네임입니다.");
                    } else {
                        setNicknameHelperText("사용할 수 있는 닉네임입니다.");
                    }
                })
                .catch(() => {
                    setNicknameHelperText("닉네임의 길이를 30자 이하로 입력해주세요.");
                });
        },
        () => setNicknameHelperText("닉네임을 입력하세요."),
        200
    );

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
        { type: "email", label: "EMAIL", helperText: emailHelperText, onFormData: setEmail },
        { type: "password", label: "PASSWORD", helperText: passwordHelperText, onFormData: setPassword },
        { type: "nickname", label: "NICKNAME", helperText: nicknameHelperText, onFormData: setNickname },
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
