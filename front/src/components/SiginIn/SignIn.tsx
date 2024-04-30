import { Box, styled } from "@mui/material";
import SignInTitle from "./SignInTItle";
import SignInForm from "./SignInForm";

const SignInBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "66vh",
    height: "530px",
    borderRadius: "8px",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
});

const SignIn = () => {
    return (
        <SignInBox>
            <SignInTitle></SignInTitle>
            <SignInForm></SignInForm>
        </SignInBox>
    );
};

export default SignIn;
