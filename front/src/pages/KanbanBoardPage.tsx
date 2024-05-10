import {
    Container,
} from "@mui/material";

import KanbanBoadrFilter from "../components/KanbanBoard/KanbanBoadrFilter.tsx";
import KanbanBoard from "../components/KanbanBoard/KanbanBoard.tsx";
import {useKanbanBoardStore} from "../stores/KanbanBoardStorage.tsx";
import {MemberProp} from "../types/Member/Member.ts";
import {useEffect} from "react";
import {Stomp} from "@stomp/stompjs";


type KanbanBoardProps = {
    projectId: number;
    moduleId: number;
}


// FixMe 프로젝트 멤버 조회 api 호출로 바꾸기
const projectMembers: MemberProp[] = [
    {
        id: 1,
        nickname: "슈밤",
        profile: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHcLQLjBSyQ2kk7X4xV_XxScm1nk0TUqqUoUtqSTIQg&s",
    },
    {
        id: 2,
        nickname: "단밤",
        profile: "https://cdn.hankyung.com/photo/202403/01.36047379.1.jpg",
    },
    {
        id: 3,
        nickname: "시밤",
        profile: "https://blog.kakaocdn.net/dn/biKQiJ/btskhCTo1yV/p4tySh1KUUCyOfd7Y6LYUK/img.jpg",
    },
]

const KanbanBoardPage = ({projectId, moduleId}: KanbanBoardProps) => {
    const {
        setClient,
        currentProjectId,
        setCurrentProjectId,
        kanbanBoardId,
        setKanbanBoardId,
        setprojectMembers,
        getKanbanBoard,
        getLabels,
    } = useKanbanBoardStore();
    const serverUrl = "localhost:8080"

    useEffect(() => {
        setKanbanBoardId(moduleId);
        setCurrentProjectId(projectId);

        if (kanbanBoardId && currentProjectId) {
            getKanbanBoard(currentProjectId, kanbanBoardId);
            getLabels(currentProjectId, kanbanBoardId);
            setprojectMembers(projectMembers);
        }
    }, [moduleId, kanbanBoardId, projectId]);


    // stomp 칸반보드 구독
    useEffect(() => {
        const sock = new WebSocket(`ws://${serverUrl}/ws`);
        setClient(Stomp.over(() => sock));

    }, [kanbanBoardId]);


    return (
        <Container style={{padding: 20}}>
            <KanbanBoadrFilter/>
            <KanbanBoard/>
        </Container>
    );
}

export default KanbanBoardPage;