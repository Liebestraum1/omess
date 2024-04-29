import TextField from "@mui/material/TextField";
import ClearIcon from "@mui/icons-material/Clear";
import { styled } from "@mui/material";
import { FormInputProp } from "../../types/common/FormProps";

const DimClearIcon = styled(ClearIcon)({
    color: "grey",
});

const FormInput = ({ type, label, helperText, onFormData }: FormInputProp) => {
    return (
        <TextField
            id={"input-with-icon-textfield-helperText " + type}
            label={label}
            variant="standard"
            helperText={helperText}
            InputProps={{
                endAdornment: <DimClearIcon />,
            }}
            onChange={(e) => onFormData(e.target.value)}
            type={type}
        />
    );
};

export default FormInput;
