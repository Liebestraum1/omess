import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import {
    Alert,
    Button,
    Dialog, DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton,
    Modal,
    Snackbar
} from "@mui/material";
import {ChangeEvent, useState} from "react";
import {deleteDomain, updateDomain} from "../request/ApiSpecificationRequest.ts";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

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
    p: 4,
    display: 'flex',
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
    const [isShowMode, setIsShowMode] = useState<boolean>(true)
    const [isDeleteMode, setIsDeleteMode] = useState<boolean>(false)
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
        setIsShowMode(true)
        setInputValue(name)
        setIsDeleteMode(false)
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

    const handleDeleteDomain = () => {
        deleteDomain(projectId, apiSpecificationId, domainId)
            .then(() => {
                handleClose()
                onChildChange()
            })
            .catch((e) => {
                setErrorState({isError: true, errorMessage: e.response.data.title})
            })
    }

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
                        value={inputValue}
                        onChange={handleChange}
                        disabled={isShowMode}
                        inputProps={{
                            maxLength: 20
                        }}
                        error={isSameName}
                        helperText={isSameName ? '동일한 이름입니다.' : ''}
                    />
                    <IconButton
                        aria-label="modifiy"
                        color='warning'
                        sx={{padding: 0, display: isShowMode && !isDeleteMode ? 'block' : 'none'}}
                        onClick={() => setIsShowMode(false)}
                    >
                        <EditIcon/>
                    </IconButton>
                    <IconButton
                        aria-label="delete"
                        color='error'
                        sx={{padding: 0, display: isShowMode && !isDeleteMode? 'block' : 'none'}}
                        onClick={() => setIsDeleteMode(true)}
                    >
                        <DeleteIcon/>
                    </IconButton>
                    <Button
                        variant="outlined"
                        onClick={() => {
                            setIsShowMode(true)
                            setIsDeleteMode(false)
                            setInputValue(name)
                        }}
                        color='secondary'
                        sx={{width: '25%', height: '55px', display: !isShowMode ? 'block' : 'none'}}
                    >
                        취소
                    </Button>
                    <Button
                        variant="outlined"
                        onClick={handleSubmit}
                        sx={{width: '25%', height: '55px', display: !isShowMode ? 'block' : 'none'}}
                    >
                        저장
                    </Button>

                    <Dialog
                        open={isDeleteMode}
                        onClose={()=> setIsDeleteMode(false)}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogTitle id="alert-dialog-title">
                            {"도메인을 삭제하시겠습니까?"}
                        </DialogTitle>
                        <DialogContent>
                            <DialogContentText id="alert-dialog-description">
                                도메인 삭제 시, 도메인 하위에 작성된 API들도 함께 삭제됩니다.
                            </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                            <Button color='secondary' onClick={() => setIsDeleteMode(false)}>취소</Button>
                            <Button color='error' onClick={() => handleDeleteDomain()} autoFocus>
                                삭제
                            </Button>
                        </DialogActions>
                    </Dialog>
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
                        sx={{ width: '100%' }}
                    >
                        {errorState.errorMessage}
                    </Alert>
                </Snackbar>
            </Box>
        </Modal>
    )

}

export default UpdateDomainModal