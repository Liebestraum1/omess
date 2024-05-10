import Box from "@mui/material/Box";
import {
    Backdrop, Button, IconButton,
    Modal, Snackbar,
    TextField,
    Typography
} from "@mui/material";

import {ChangeEvent, Fragment, useEffect, useState} from "react";
import MDEditor from "@uiw/react-md-editor";
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";
import {UpdateIssueRequest} from "../../../types/Issue/UpdateIssueRequest.ts";
import CloseIcon from '@mui/icons-material/Close';

type IssueDetailModalProp = {
    open: boolean;
    onClose: () => void;
    originTitle: string;
    originContent: string;
};

const IssueUpdateModal = ({open, onClose, originTitle, originContent}: IssueDetailModalProp) => {
    const [title, setTitle] = useState<string>("");
    const [md, setMd] = useState<string | undefined>("");
    const {currentProjectId, updateIssue, kanbanBoardId, issueId, setIssuedId} = useKanbanBoardStore();
    const onCloseModal = () => {
        setIssuedId(null);
        onClose();
    }
    const handleChangeTitle = (event: ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    const setIssueInfo = () => {
        setTitle(originTitle);
        setMd(originContent ? originContent : "");
    }

    useEffect(() => {
        setIssueInfo();
    }, [originTitle, originContent]);

    function onClickCreateIssue() {
        const updateIssueRequest: UpdateIssueRequest = {
            title: title,
            content: md ? md : "",
        }

        if (currentProjectId && kanbanBoardId && issueId) {
            if (!title) {
                handleClick();
            } else {
                updateIssue(currentProjectId, kanbanBoardId, issueId, updateIssueRequest);

                onCloseModal();
            }

        }
    }

    // 유효성 검사
    const [snackbarOpen, setsnackbarOpen] = useState(false);
    const handleClick = () => {
        setsnackbarOpen(true);
    };

    const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }

        setsnackbarOpen(false);
    };
    const action = (
        <Fragment>
            <IconButton
                size="small"
                aria-label="close"
                color="inherit"
                onClick={handleClose}
            >
                <CloseIcon fontSize="small"/>
            </IconButton>
        </Fragment>
    );

    return (
        <Box>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={onCloseModal}
                closeAfterTransition
                slots={{backdrop: Backdrop}}
                slotProps={{
                    backdrop: {
                        timeout: 500,
                    },
                }}
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: "80%",
                    height: "80%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto' // 스크롤 활성화
                }}>
                    <Box paddingBottom={2}>
                        <Typography fontWeight="bold" paddingBottom={1}>제목</Typography>
                        <TextField id="standard-basic" onChange={handleChangeTitle} fullWidth
                                   defaultValue={title}> </TextField>
                    </Box>
                    <Box sx={{
                        maxHeight: 500, // 최대 높이 설정
                        overflowY: 'auto', // 내용이 많을 경우 스크롤
                        paddingBottom: 5
                    }}>
                        <Typography fontWeight="bold" paddingBottom={1}>설명</Typography>
                        <div className="markarea">
                            <div data-color-mode="light">
                                <MDEditor height={350} value={md} onChange={setMd}/>
                            </div>
                        </div>
                    </Box>
                    <Box display={"flex"} justifyContent={"end"} alignItems={"center"}>
                        <Button variant="outlined" color="secondary" sx={{height: 49, paddingTop: 1.1}}
                                onClick={onClickCreateIssue}> 이슈 수정 </Button>
                    </Box>
                </Box>
            </Modal>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleClose}
                message="제목은 공백일 수 없습니다."
                action={action}
            />
        </Box>
    );
}

export default IssueUpdateModal;
