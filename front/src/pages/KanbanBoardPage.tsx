import {
    Button,
    Container,
} from "@mui/material";

import KanbanBoadrFilter from "../components/KanbanBoard/KanbanBoadrFilter.tsx";
import KanbanBoard from "../components/KanbanBoard/KanbanBoard.tsx";
import {useKanbanBoardStore} from "../stores/KanbanBoardStorage.tsx";
import {MemberProp} from "../types/Member/Member.ts";
import {useEffect, useRef} from "react";
import {CompatClient, Stomp} from "@stomp/stompjs";


type KanbanBoardProps = {
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

const KanbanBoardPage = ({moduleId}: KanbanBoardProps) => {
    const {
        setprojectMembers,
        selectedLabel,
        selectedMember,
        selectedImpotance,
        getKanbanBoard,
        getIssues,
        getLabels,
        setKanbanBoardId,
        kanbanBoardId
    } = useKanbanBoardStore();
    const serverUrl = "localhost:8080/api/v1"

    useEffect(() => {
        console.log(moduleId)
        setKanbanBoardId(moduleId);
        console.log(kanbanBoardId)
        if(kanbanBoardId){
            getKanbanBoard(1, kanbanBoardId);
            getLabels(1, kanbanBoardId);
            setprojectMembers(projectMembers);
        }
    }, [kanbanBoardId, setKanbanBoardId, getKanbanBoard, getLabels, setprojectMembers]);


    // stomp 칸반보드 구독
    const stompClient = useRef<CompatClient>();

    useEffect(() => {
        const sock = new WebSocket(`ws://${serverUrl}/ws`);
        stompClient.current = Stomp.over(() => sock);
        stompClient.current.connect(
            {},
            () => {
                stompClient.current && stompClient.current.subscribe('/sub/kanbanRoom/' + kanbanBoardId, () => {
                    getIssues(1, moduleId, selectedMember, selectedLabel, selectedImpotance);
                });
            })
    }, [kanbanBoardId, stompClient, getIssues, selectedMember, selectedLabel, selectedImpotance]);

    const sendMessage = () => {
        if (stompClient.current && stompClient.current.connected) {
            const issueRequest = {
                title: "New Issue",
                content: "Description of new issue",
                importance: 1,
                status: 1,
            };

            // STOMP 메시지 전송
            stompClient.current.send('/pub/kanbanboards/' + kanbanBoardId,
                {},
                JSON.stringify(issueRequest)
            );
            console.log('Message sent!');
        } else if (!stompClient) {
            console.log('Not connected to WebSocket');
        } else {
            console.log('ss');
        }
    };

    return (
        <Container style={{padding: 20}}>
            <Button onClick={sendMessage}>버튼</Button>
            <KanbanBoadrFilter/>
            <KanbanBoard/>
        </Container>
    );
}

export default KanbanBoardPage;