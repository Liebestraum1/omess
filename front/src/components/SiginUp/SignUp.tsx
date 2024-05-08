import { Box, styled } from "@mui/material";
import SignUpTitle from "./SignUpTItle";
import SignUpForm from "./SignUpForm";
import { SignUpProps } from "../../types/SignUp/SignUp";

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

const SignUp = ({ onClose, showAlert, setAlertContent }: SignUpProps) => {
    return (
        <SignUpBox>
            <SignUpTitle></SignUpTitle>
            <SignUpForm onClose={onClose} setAlertContent={setAlertContent} showAlert={showAlert}></SignUpForm>
        </SignUpBox>
    );
};

export default SignUp;
