import Box from "@mui/material/Box";
import {
    Alert,
    Button,
    Chip,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton, Snackbar
} from "@mui/material";
import {useState} from "react";
import UpdateDomainModal from "./UpdateDomainModal.tsx";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import {deleteDomain} from "../request/ApiSpecificationRequest.ts";

interface ErrorState{
    isError: boolean,
    errorMessage: string
}

const DomainComponent = (
    {projectId, apiSpecificationId, domainId, name, onChildChange}:
        { projectId: number, apiSpecificationId: number, domainId: number, name: string, onChildChange: () => void}
) => {
    const [open, setOpen] = useState<boolean>(false);
    const [isDeleteMode, setIsDeleteMode] = useState<boolean>(false)
    const [errorState, setErrorState] = useState<ErrorState>({
        isError: false,
        errorMessage: ''
    })

    const changeOpen = (isOpen: boolean) => {
        setOpen(isOpen)
    }

    const handleDeleteDomain = () => {
        deleteDomain(projectId, apiSpecificationId, domainId)
            .then(() => {
                setIsDeleteMode(false)
                onChildChange()
            })
            .catch((e) => {
                setErrorState({isError: true, errorMessage: e.response.data.title})
            })
    }

    return (
        <Box
            display="flex"
            marginRight='auto'
            marginBottom='1rem'
            justifyContent='center'
            alignItems='center'
        >
            <Chip
                label={name}
                variant="outlined"
            />
            <Box>
                <IconButton
                    aria-label="modifiy"
                    sx={{padding : 0, ml: 1}}
                    onClick={() => setOpen(true)}
                >
                    <EditIcon/>
                </IconButton>
                <IconButton
                    aria-label="delete"
                    color='error'
                    sx={{padding : 0, ml: 1}}
                    onClick={() => setIsDeleteMode(true)}
                >
                    <DeleteIcon/>
                </IconButton>
            </Box>

            <UpdateDomainModal
                projectId={projectId}
                apiSpecificationId={apiSpecificationId}
                domainId={domainId}
                name={name}
                open={open}
                onChildChange={onChildChange}
                changeOpen={changeOpen}
            />
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
    )
}


export default DomainComponent