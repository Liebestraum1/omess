import Typography from "@mui/material/Typography";
import { Fragment } from "react/jsx-runtime";

const SignInTitle = () => {
    return (
        <Fragment>
            <Typography
                sx={{
                    fontSize: "48px",
                }}
            >
                서버 가입
            </Typography>
            <Typography id="modal-modal-description">Omess에 오신 것을 환영합니다.</Typography>
        </Fragment>
    );
};

export default SignInTitle;
