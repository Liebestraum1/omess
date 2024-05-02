import { Alert, AlertColor, AlertTitle, Button, Snackbar } from "@mui/material";

export type AlertProp = {
    severity: AlertColor | undefined;
    title: string;
    content: string;
};

type SignSnackBarProp = {
    alertProp: AlertProp;
    open: boolean;
    onClose: (status: boolean) => void;
};

const alertButton = (action: () => void) => {
    return <Button onClick={action}> 확인 </Button>;
};

const SignSnackBar = ({ alertProp, open, onClose }: SignSnackBarProp) => {
    return (
        <Snackbar
            open={open}
            onClose={() => onClose(false)}
            anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
            autoHideDuration={3000}
        >
            <Alert severity={alertProp.severity} action={alertButton(() => onClose(false))}>
                <AlertTitle>{alertProp.title}</AlertTitle>
                {alertProp.content}
            </Alert>
        </Snackbar>
    );
};

export default SignSnackBar;
