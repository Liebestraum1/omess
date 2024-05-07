import Box from "@mui/material/Box";
import {useEffect, useState} from "react";
import IssueDetailModal from "../Issue/IssueDetail/IssueDetailModal.tsx";
import IssueList from "../Issue/IssueList.tsx";
import {IssueProp} from "../../types/Issue/Issue.ts";
import IssueCreateModal from "../Issue/IssueCreate/IssueCreateModal.tsx";
import Grid from '@mui/material/Grid';
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";

const KanbanBoard = () => {
    
    // 이슈 디테일 모달
    const [selectedIssue, setSelectedIssue] = useState(0);
    const [open, setOpen] = useState(false);

    const handleClickOpen = (issueId: number) => {
        setSelectedIssue(issueId);
        setOpen(true);
    }

    const handleClose = () => {
        setSelectedIssue(0);
        setOpen(false);
    }

    // 이슈 생성 모달
    const [openCreate, setOpenCreate] = useState(false);

    const handleClickOpenCreate = () => {
        setOpenCreate(true);
    }

    const handleCloseOpenCreate = () => {
        setOpenCreate(false);
    }
    
    // 칸반보드 이슈 진행도로 나누기
    const {issues} = useKanbanBoardStore();
    const [Beforeproceeding, setBeforeproceeding] = useState<IssueProp[]>([])
    const [Proceeding, setProceeding] = useState<IssueProp[]>([])
    const [Complete, setComplete] = useState<IssueProp[]>([])
    useEffect(() => {
        if (issues) {
            setBeforeproceeding(issues.filter(issue => issue.status === 1));
            setProceeding(issues.filter(issue => issue.status === 2));
            setComplete(issues.filter(issue => issue.status === 3));
        }
    }, [issues]);

    return (
        <Box>
            <Grid container columnSpacing={3} justifyContent="center">
                {/** 진행전 */}
                <IssueList title={"진행전"} issues={Beforeproceeding} handleClickOpen={handleClickOpen}
                           handleClickOpenCreate={handleClickOpenCreate}/>

                {/** 진행중 */}
                <IssueList title={"진행중"} issues={Proceeding} handleClickOpen={handleClickOpen}
                           handleClickOpenCreate={handleClickOpenCreate}/>

                {/** 완료 */}
                <IssueList title={"완료"} issues={Complete} handleClickOpen={handleClickOpen}
                           handleClickOpenCreate={handleClickOpenCreate}/>

            </Grid>
            <IssueDetailModal open={open} issueId={selectedIssue} onClose={handleClose}/>
            <IssueCreateModal open={openCreate} onClose={handleCloseOpenCreate}/>
        </Box>
    );
}

export default KanbanBoard;