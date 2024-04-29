import { Button } from "@mui/material";
const SignInFormSubmitButton = () => {
    return (
        <Button
            sx={{
                marginTop: "36px",
                fontSize: 16,
                width: "231px",
            }}
            variant="contained"
            type="submit"
        >
            가입하기
        </Button>
    );
};

export default SignInFormSubmitButton;
