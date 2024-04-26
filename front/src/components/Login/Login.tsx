import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import omessLogo from "../../assets/omess_logo.png";
import { Button, Typography, styled } from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";

type LoginTextProps = {
    title: string;
    text: string;
};

const LoginFormTextField = styled(TextField)({
    marginBlock: 2,
});

const DimClearIcon = styled(ClearIcon)({
    color: "grey",
});

const LoginLogo = () => {
    return (
        <Box
            sx={{
                width: "100%",
                display: "flex",
                justifyContent: "center",
                marginTop: "5%",
            }}
        >
            <img src={omessLogo}></img>
        </Box>
    );
};

const LoginText = ({ title, text }: LoginTextProps) => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
            }}
        >
            <Typography
                sx={{
                    fontSize: 48,
                    fontWeight: "medium",
                }}
            >
                {title}
            </Typography>
            <Typography sx={{}}>{text}</Typography>
        </Box>
    );
};

const LoginForm = () => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                width: "33%",
            }}
        >
            <LoginFormTextField
                id="input-with-icon-textfield-helperText"
                label="EMAIL"
                variant="standard"
                helperText="도움말 삽입하기"
                InputProps={{
                    endAdornment: <DimClearIcon />,
                }}
            />
            <LoginFormTextField
                id="standard-helperText"
                label="PASSWORD"
                variant="standard"
                helperText="도움말 삽입하기"
                InputProps={{
                    endAdornment: <DimClearIcon />,
                }}
            />
            <Button
                sx={{
                    marginTop: "36px",
                    fontSize: 16,
                }}
                variant="contained"
            >
                Login
            </Button>
        </Box>
    );
};

const Login = () => {
    return (
        <Box
            sx={{
                width: "100%",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
            }}
        >
            <LoginLogo></LoginLogo>
            <LoginText title={"로그인"} text={"이메일과 비밀번호를 입력하세요."}></LoginText>
            <LoginForm></LoginForm>
        </Box>
    );
};

export default Login;
