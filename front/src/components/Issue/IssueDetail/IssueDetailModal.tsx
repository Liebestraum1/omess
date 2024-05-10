import Box from "@mui/material/Box";
import {
    Backdrop, Button,
    Divider, Menu, MenuItem,
    Modal,
    Rating,
    Typography
} from "@mui/material";
import {IssueDetailProp} from "../../../types/Issue/IssueDetail.ts";
import IssueLabelFilter from "./IssueLabelFilter.tsx";
import IssueChargerFilter from "./IssueChargerFilter.tsx";
import Grid from '@mui/material/Grid';
import MDEditor from "@uiw/react-md-editor";
import IssueStatusFilter from "./IssueStatusFilter.tsx";
import React, {useEffect, useState} from "react";
import IssueUpdateModal from "../IssueUpdate/IssueUpdateModal.tsx";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";


type IssueDetailModalProp = {
    open: boolean;
    onClose: () => void;
};

const IssueDetailModal = ({open, onClose}: IssueDetailModalProp) => {
    const {
        currentProjectId,
        getIssueDetail,
        deleteIssue,
        updateIssueImportance,
        kanbanBoardId,
        issueId,
        setIssuedId
    } = useKanbanBoardStore();
    const [openUpdate, setOpenUpdate] = useState(false);
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [issueDetail, setIssueDetail] = useState<IssueDetailProp>({
        issueId: 0,
        title: "",
        content: "",
        charger: {
            id: 0,
            nickname: "",
            profile: ""
        },
        label: {
            labelId: 0,
            name: "",
        },
        importance: 0,
        status: 0
    });

    // 메뉴 여닫기
    const handleClickOpenMenu = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleCloseMenu = () => {
        setAnchorEl(null);
    };

    const handelDetailClose = () => {
        setIssuedId(null);
        onClose();
    }

    // 수정하기
    const handleClickOpen = () => {
        handleCloseMenu();
        onClose();
        setOpenUpdate(true);
    }

    const handleClose = () => {
        setIssuedId(null);
        setOpenUpdate(false);
    }


    // 이슈 중요도 수정
    const onClickRating = (newValue: number | null) => {
        if (currentProjectId && kanbanBoardId && issueId && newValue) {
            updateIssueImportance(currentProjectId, kanbanBoardId, issueId, newValue);
            setIssueDetail({
                ...issueDetail,
                importance: newValue
            })
        }
    }


    //삭제하기
    const handleClickDelete = () => {
        handleCloseMenu();
        if (currentProjectId && kanbanBoardId && issueId) {
            deleteIssue(currentProjectId, kanbanBoardId, issueId);
            setIssuedId(null);
            onClose();
        }
    }
    const getDetail = async () => {
        if (currentProjectId && kanbanBoardId && issueId && issueId > 0) {
            const response = await getIssueDetail(currentProjectId, kanbanBoardId, issueId);

            setIssueDetail(response);
        }
    }

    useEffect(() => {
        if (issueId != null) {
            getDetail();
        }
    }, [issueId]);


    return (
        <Box>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={handelDetailClose}
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
                    height: "70%",
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    overflowY: 'auto' // 스크롤 활성화
                }}>
                    <Box display={"flex"} justifyContent={"space-between"}>
                        <Typography variant="h4" paddingBottom={3}> {issueDetail!.title} </Typography>
                        <Box>
                            <Button
                                id="basic-button"
                                aria-controls={open ? 'basic-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                                onClick={handleClickOpenMenu}
                            >
                                <MoreVertIcon color="secondary"/>
                            </Button>
                            <Menu
                                id="basic-menu"
                                anchorEl={anchorEl}
                                open={Boolean(anchorEl)}
                                onClose={handleCloseMenu}
                                MenuListProps={{
                                    'aria-labelledby': 'basic-button',
                                }}
                            >
                                <MenuItem onClick={handleClickOpen}>수정하기</MenuItem>
                                <MenuItem onClick={handleClickDelete}>삭제하기</MenuItem>
                            </Menu>
                        </Box>
                    </Box>
                    <Divider/>
                    <Grid container paddingY={2}>
                        <Grid item xs={10}>
                            <Box sx={{
                                overflowY: 'auto' // 내용이 많을 경우 스크롤
                            }}>
                                <Typography fontWeight="bold" paddingBottom={1}>설명</Typography>
                                <div className="markarea">
                                    <div data-color-mode="light">
                                        <MDEditor.Markdown source={issueDetail!.content}/>
                                    </div>
                                </div>
                            </Box>
                        </Grid>
                        <Grid item xs={2} paddingLeft={1}>
                            <Box justifyContent={"center"}>
                                <IssueChargerFilter id={issueDetail.charger?.id || null}/>
                                <IssueLabelFilter labelId={issueDetail?.label?.labelId || null}/>
                                <IssueStatusFilter status={issueDetail!.status}/>
                                <Box paddingLeft={7}>
                                    <Rating name="read-only" size="large" value={issueDetail.importance}
                                            onChange={(event, newValue) => {
                                                onClickRating(newValue);
                                            }} max={3}/>
                                </Box>
                            </Box>
                        </Grid>
                    </Grid>
                </Box>
            </Modal>

            <IssueUpdateModal open={openUpdate} onClose={handleClose} originContent={issueDetail.content}
                              originTitle={issueDetail.title}/>
        </Box>
    );
}

export default IssueDetailModal;
