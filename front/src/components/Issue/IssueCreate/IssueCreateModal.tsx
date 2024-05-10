import Box from "@mui/material/Box";
import {
    Backdrop, Button, IconButton,
    Modal, Snackbar,
    TextField,
    Typography
} from "@mui/material";

import {ChangeEvent, Fragment, useState} from "react";
import MDEditor from "@uiw/react-md-editor";
import IssueCreateStatusFilter from "./IssueCreateStatusFilter.tsx";
import IssueCreateImportanceFilter from "./IssueCreateImportanceFilter.tsx";
import IssueCreateChargerFilter from "./IssueCreateChargerFilter.tsx";
import IssueCreateLabelFilter from "./IssueCreateLabelFilter.tsx";
import {CreateIssueProp} from "../../../types/Issue/CreateIssue.ts";
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";
import CloseIcon from "@mui/icons-material/Close";

type IssueDetailModalProp = {
    open: boolean;
    onClose: () => void;
};

const IssueCreateModal = ({open, onClose}: IssueDetailModalProp) => {
    const [title, setTitle] = useState<string>("");
    const [md, setMd] = useState<string | undefined>("");
    const [status, setStatus] = useState<number | null>(null);
    const [importance, setImportance] = useState<number | null>(null);
    const [charger, setCharger] = useState<number | null>(null);
    const [label, setLabel] = useState<number | null>(null);
    const {createIssue, currentProjectId, kanbanBoardId} = useKanbanBoardStore();
    const onCloseModal = () => {
        setMd("");
        onClose();
    }
    const handleChangeTitle = (event: ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    const onClickCreateIssue = () => {
        if (status && importance && title) {
            if (md === undefined) {
                setMd("");
            }
            const writeIssueRequest: CreateIssueProp = {
                title: title,
                content: md!,
                importance: importance,
                status: status,
                memberId: charger,
                labelId: label
            }
            if (kanbanBoardId && currentProjectId) {
                console.log(writeIssueRequest);
                createIssue(currentProjectId, kanbanBoardId, writeIssueRequest);
                onCloseModal();
            }
        } else if (!title) {
            setMessage("제목은 공백일 수 없습니다.")
            handleClick();
        } else if (!status) {
            setMessage("진행 상태는 공백일 수 없습니다.")
            handleClick();
        } else if (!importance) {
            setMessage("중요도는 공백일 수 없습니다.")
            handleClick();
        }

    }

    // 유효성 검사
    const [snackbarOpen, setsnackbarOpen] = useState(false);
    const [message, setMessage] = useState("");
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
                        <TextField id="standard-basic" onChange={handleChangeTitle} fullWidth> </TextField>
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
                    <Box display={"flex"} justifyContent={"space-between"} alignItems={"center"}>
                        <Box display={"flex"}>
                            <IssueCreateChargerFilter onChangeCharger={setCharger}/>
                            <IssueCreateLabelFilter onChangeLabel={setLabel}/>
                            <IssueCreateImportanceFilter onChangeImportance={setImportance}/>
                            <IssueCreateStatusFilter onChangeStatus={setStatus}/>
                        </Box>
                        <Button variant="outlined" color="secondary" sx={{height: 49, paddingTop: 1.1}}
                                onClick={onClickCreateIssue}> 이슈 생성 </Button>
                    </Box>
                </Box>
            </Modal>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleClose}
                message={message}
                action={action}
            />
        </Box>
    );
}

export default IssueCreateModal;
