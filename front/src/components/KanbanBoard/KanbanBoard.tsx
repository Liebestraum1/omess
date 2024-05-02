import Grid from "@mui/material/Unstable_Grid2";
import Box from "@mui/material/Box";
import {useState} from "react";
import IssueDetailModal from "../Issue/IssueDetailModal.tsx";
import IssueList from "../Issue/IssueList.tsx";
import {IssueProp} from "../../types/Issue/Issue.ts";

const KanbanBoard = () => {
    const [open, setOpen] = useState(false);
    const [selectedIssue, setSelectedIssue] = useState(0);

    const handleClickOpen = (issueId:number) => {
        setSelectedIssue(issueId);
        setOpen(true);
    }

    const handleClose = () => {
        setSelectedIssue(0);
        setOpen(false);
    }
    // FixMe 이슈 리스트 api 호출
    const Beforeproceeding: IssueProp[] = [
        {
            issueId: 1,
            title: "[BackEnd] 회원가입 api 구현",
            charger: {
                id: 1,
                nickname: "슈밤",
                profile: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"
            },
            label: {
                labelId: 1,
                name: "Back-End"
            },
            importance: 1,
            status: 1,
        }
    ]

    const Proceeding: IssueProp[] = [
        {
            issueId: 2,
            title: "[BackEnd] 로그인 api 구현",
            charger: {
                id: 1,
                nickname: "슈밤",
                profile: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s"
            },
            label: {
                labelId: 1,
                name: "Back-End"
            },
            importance: 2,
            status: 2,
        },
        {
            issueId: 3,
            title: "[FrontEnd] 로그인 api 구현",
            charger: {
                id: 2,
                nickname: "담밤",
                profile: "https://cdn.hankyung.com/photo/202403/01.36047379.1.jpg"
            },
            label: {
                labelId: 2,
                name: "Front-End"
            },
            importance: 3,
            status: 2,
        },
    ]

    const Complete: IssueProp[] = [
        {
            issueId: 4,
            title: "[BackEnd] 게시판 생성 api 구현",
            charger: {
                id: 3,
                nickname: "담밤",
                profile: "https://i.ytimg.com/vi/fnjyQzSja7o/maxresdefault.jpg"
            },
            label: {
                labelId: 1,
                name: "Back-End"
            },
            importance: 2,
            status: 2,
        },
        {
            issueId: 5,
            title: "[FrontEnd] 메인화면 구현",
            charger: {
                id: 2,
                nickname: "담밤",
                profile: "https://cdn.hankyung.com/photo/202403/01.36047379.1.jpg"
            },
            label: {
                labelId: 2,
                name: "Front-End"
            },
            importance: 3,
            status: 2,
        },
    ]



    return (
        <Box>
            <Grid container columnSpacing={3} justifyContent="center">
                {/** 진행전 */}
                <IssueList title={"진행전"} issues={Beforeproceeding} handleClickOpen={handleClickOpen}/>

                {/** 진행중 */}
                <IssueList title={"진행중"} issues={Proceeding} handleClickOpen={handleClickOpen}/>

                {/** 완료 */}
                <IssueList title={"완료"} issues={Complete} handleClickOpen={handleClickOpen}/>

            </Grid>
            <IssueDetailModal open={open} issueId={selectedIssue} onClose={handleClose}/>
        </Box>
    );
}

export default KanbanBoard;