import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import {
    Alert,
    Button,
    Modal,
    Snackbar
} from "@mui/material";
import {ChangeEvent, useState} from "react";
import {updateDomain} from "../request/ApiSpecificationRequest.ts";

interface ErrorState{
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
    paddingTop: 4,
    px: 2,
    display: 'flex',
    flexDirection: 'column',
    gap: 2,
};

const UpdateDomainModal = (
    {projectId, apiSpecificationId, domainId, name, onChildChange, open, changeOpen}:
        {
            projectId: number,
            apiSpecificationId: number,
            domainId: number,
            name: string,
            onChildChange: () => void,
            open: boolean,
            changeOpen: (isOpen: boolean) => void
        }
) => {
    const [inputValue, setInputValue] = useState<string>(name);
    const [isSameName, setIsSameName] = useState<boolean>(false);
    const [errorState, setErrorState] = useState<ErrorState>({
        isError: false,
        errorMessage: ''
    })
    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
        if (name === event.target.value) {
            setIsSameName(true)
        } else {
            setIsSameName(false)
        }
    };

    const handleClose = () => {
        setInputValue(name)
        changeOpen(false)
    }

    const handleSubmit = () => {
        if (!isSameName) {
            updateDomain(projectId, apiSpecificationId, domainId, inputValue)
                .then(() => {
                    handleClose()
                    setInputValue(inputValue)
                    onChildChange()
                })
                .catch((e) => {
                        setErrorState({isError: true, errorMessage: e.response.data.title})
                    }
                )
        }
    };



    return (
        <>
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
                            value={inputValue}
                            onChange={handleChange}
                            inputProps={{
                                maxLength: 20
                            }}
                            error={isSameName}
                            helperText={isSameName ? '동일한 이름입니다.' : ''}
                        />

                        <Box sx={{display: 'flex',justifyContent: 'flex-end'}}>
                            <Button
                                onClick={handleClose}
                                color='secondary'
                            >
                                취소
                            </Button>
                            <Button
                                onClick={handleSubmit}
                            >
                                저장
                            </Button>
                        </Box>
                    </Box>

                </Box>
            </Modal>
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
                    sx={{ width: '100%' }}
                >
                    {errorState.errorMessage}
                </Alert>
            </Snackbar>
        </>
    )

}

export default UpdateDomainModal