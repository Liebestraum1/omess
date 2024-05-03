import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import {
    Alert,
    Button,
    Modal,
    Snackbar
} from "@mui/material";
import {ChangeEvent, useState} from "react";
import {createDomain} from "../request/ApiSpecificationRequest.ts";

interface ErrorState {
    isError: boolean,
    errorMessage: string
}

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    borderRadius: '5px',
    boxShadow: 24,
    p: 4,
    display: 'flex',
    gap: 2,
};

const CreateDomainModal = (
    {projectId, apiSpecificationId, onChildChange, open, changeOpen}:
        {
            projectId: number,
            apiSpecificationId: number,
            onChildChange: () => void,
            open: boolean,
            changeOpen: (isOpen: boolean) => void
        }
) => {

    const [inputValue, setInputValue] = useState<string>('')
    const [errorState, setErrorState] = useState<ErrorState>({
        isError: false,
        errorMessage: ''
    })
    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value)
    };

    const handleClose = () => {
        changeOpen(false)
    }

    const handleSubmit = () => {
        createDomain(projectId, apiSpecificationId, inputValue)
            .then(() => {
                handleClose()
                onChildChange()
            })
            .catch((e) => {
                    setErrorState({isError: true, errorMessage: e.response.data.title})
                }
            )
    };

    return (
        <Modal
            open={open}
            onClose={handleClose}
        >
            <Box>
                <Box sx={modalStyle}>
                    <TextField
                        fullWidth
                        label="도메인 명(최대 20자)"
                        variant="outlined"
                        onChange={handleChange}
                        inputProps={{
                            maxLength: 20
                        }}
                    />

                    <Button
                        variant="outlined"
                        onClick={handleClose}
                        color='secondary'
                        sx={{width: '25%', height: '55px'}}
                    >
                        취소
                    </Button>
                    <Button
                        variant="outlined"
                        onClick={handleSubmit}
                        sx={{width: '25%', height: '55px'}}
                    >
                        추가
                    </Button>

                </Box>
                <Snackbar
                    open={errorState.isError}
                    autoHideDuration={2000}
                    onClose={() => {
                        setErrorState({isError: false, errorMessage: ''})
                    }}
                    anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
                >
                    <Alert
                        severity="error"
                        onClose={() => {
                            setErrorState({isError: false, errorMessage: ''})
                        }}
                        variant="filled"
                        sx={{width: '100%'}}
                    >
                        {errorState.errorMessage}
                    </Alert>
                </Snackbar>
            </Box>
        </Modal>
    )

}

export default CreateDomainModal