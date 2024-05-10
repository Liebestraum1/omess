import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import {
    Alert,
    Button,
    Modal,
    Snackbar, Typography
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
    pt: 1,
    px: 2,
    display: 'flex',
    flexDirection: 'column',
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
                    <Typography variant='h6' component='div'>
                        도메인 생성
                    </Typography>
                    <TextField
                        fullWidth
                        label="도메인 명(최대 20자)"
                        variant="outlined"
                        onChange={handleChange}
                        inputProps={{
                            maxLength: 20
                        }}
                    />

                    <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                        <Button
                            onClick={handleClose}
                            color='secondary'
                        >
                            취소
                        </Button>
                        <Button
                            onClick={handleSubmit}
                        >
                            추가
                        </Button>
                    </Box>

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