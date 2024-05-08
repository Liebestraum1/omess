import { Button } from "@mui/material";
const SignUpFormSubmitButton = () => {
    return (
        <Button
            sx={{
                marginTop: "36px",
                fontSize: 16,
                width: "100%",
            }}
            variant="contained"
            type="submit"
        >
            가입하기
        </Button>
    );
};

export default SignUpFormSubmitButton;
