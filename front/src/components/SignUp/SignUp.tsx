import { Alert, AlertTitle, Box, Button, Snackbar, styled } from "@mui/material";
import SignUpTitle from "./SignUpTItle";
import SignUpForm from "./SignUpForm";
import { useState } from "react";
import { AlertContent } from "../../types/common/Alert";

const SignUpBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "66vh",
    height: "530px",
    borderRadius: "8px",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
});

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const SignUp = () => {
    const [showAlert, setShowAlert] = useState<boolean>(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    return (
        <SignUpBox>
            <SignUpTitle></SignUpTitle>
            <SignUpForm
                showAlert={() => setShowAlert(true)}
                setAlertContent={(alertContent: AlertContent) => setAlertContent(alertContent)}
            ></SignUpForm>

            <Snackbar
                open={showAlert}
                onClose={() => setShowAlert(false)}
                anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
            >
                <Alert severity={alertContent.severity} action={alertButton(() => setShowAlert(false))}>
                    <AlertTitle>{alertContent.title}</AlertTitle>
                    {alertContent.content}
                </Alert>
            </Snackbar>
        </SignUpBox>
    );
};

export default SignUp;
