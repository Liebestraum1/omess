import { Box, styled } from "@mui/material";
import SignUpTitle from "./SignUpTItle";
import SignUpForm from "./SignUpForm";

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

const SignUp = () => {
    return (
        <SignUpBox>
            <SignUpTitle></SignUpTitle>
            <SignUpForm></SignUpForm>
        </SignUpBox>
    );
};

export default SignUp;
