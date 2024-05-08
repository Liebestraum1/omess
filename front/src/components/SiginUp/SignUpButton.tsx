import { Alert, AlertTitle, Box, Button, Snackbar } from "@mui/material";
import { useState } from "react";
import SignUpFormModal from "./SignUpFormModal";
import { AlertContent } from "../../types/common/Alert";

const alertButton = (action: () => void) => {
    return <Button onClick={action}>확인</Button>;
};

const SignUpButton = () => {
    const [open, setOpen] = useState(false);
    const openSignUpForm = () => setOpen(true);
    const closeSignUpForm = () => setOpen(false);
    const [showAlert, setShowAlert] = useState<boolean>(false);
    const [alertContent, setAlertContent] = useState<AlertContent>({
        severity: undefined,
        title: "",
        content: "",
    });

    return (
        <Box display={"flex"} flexDirection={"column"}>
            <Button
                sx={{
                    marginTop: "16px",
                    fontSize: 16,
                }}
                variant="contained"
                onClick={openSignUpForm}
            >
                서버 가입
            </Button>

            <SignUpFormModal
                open={open}
                onClose={closeSignUpForm}
                setAlertContent={(alertContent: AlertContent) => setAlertContent(alertContent)}
                showAlert={() => setShowAlert(true)}
            />

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
        </Box>
    );
};

export default SignUpButton;
