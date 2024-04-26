import TextField from "@mui/material/TextField";
import ClearIcon from "@mui/icons-material/Clear";
import { styled } from "@mui/material";
import { LoginFormInputProp } from "../../types/Login/LoginProps";

const DimClearIcon = styled(ClearIcon)({
    color: "grey",
});

const LoginFormInput = ({ label, helperText }: LoginFormInputProp) => {
    return (
        <TextField
            id="input-with-icon-textfield-helperText"
            label={label}
            variant="standard"
            helperText={helperText}
            InputProps={{
                endAdornment: <DimClearIcon />,
            }}
        />
    );
};

export default LoginFormInput;
