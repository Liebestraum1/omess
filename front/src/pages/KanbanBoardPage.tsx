import {
    Button,
    Container,
} from "@mui/material";

import KanbanBoadrFilter from "../components/KanbanBoard/KanbanBoadrFilter.tsx";
import KanbanBoard from "../components/KanbanBoard/KanbanBoard.tsx";
import {LabelProp} from "../types/Label/Label.ts";
import {useKanbanBoardStore} from "../stores/KanbanBoardStorage.tsx";
import {MemberProp} from "../types/Member/Member.ts";
import {useEffect} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from 'sockjs-client';


type KanbanBoardProps = {
    kanbanBoardId: number;
}

// FixMe 칸반보드 라벨 리스트 조회 api 호출로 바꾸기
const labels: LabelProp[] = [
    {
        labelId: 1,
        name: "Back-End"
    },
    {
        labelId: 2,
        name: "Front-End"
    },
    {
        labelId: 3,
        name: "공통"
    },
]

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

const KanbanBoardPage = ({kanbanBoardId} : KanbanBoardProps) => {
    const { setLabels, setprojectMembers, selectedLabel, selectedMember, selectedImpotance } = useKanbanBoardStore();


    // FixMe 이슈 필터링 api 호출
    useEffect(() => {
        console.log('Selected Label Changed:', selectedLabel);
        console.log('Selected Member Changed:', selectedMember);
        console.log('Selected Importance Changed:', selectedImpotance);
        // 필요한 추가 로직을 여기에 구현
    }, [selectedLabel, selectedMember, selectedImpotance]);  // 상태 변화 감지

    useEffect(() => {
        // 페이지 진입 시 라벨과 프로젝트 멤버 설정
        setLabels(labels);
        setprojectMembers(projectMembers);
    }, [setLabels, setprojectMembers]); // 의존성 배열에 추가

    let stompClient: Client ;

    useEffect(() => {
        const sock = new SockJS('http://localhost:8080/ws');
        stompClient = new Client({
            webSocketFactory: () => sock,
            reconnectDelay: 5000,
            debug: (str) => {
                console.log('STOMP Debug:', str);
            },
        });


        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);

            // 구독 설정 예시
            stompClient.subscribe('/projects/1/kanbanboards/' + kanbanBoardId, (message) => {
                console.log('Received:', message.body);
                // 필요한 상태 업데이트 로직
            });
        };

        stompClient.onStompError = (frame) => {
            console.error('STOMP Error:', frame);
        };

        stompClient.onWebSocketError = (evt) => {
            console.error('WebSocket Error:', evt);
        };

        stompClient.onWebSocketClose = (evt) => {
            console.error('WebSocket Closed:', evt);
        };

        stompClient.activate();

        return () => {
            stompClient.deactivate();
        };

    }, [kanbanBoardId]);
    const sendMessage = () => {
        if (stompClient && stompClient.connected) {
            const issueRequest = {
                title: "New Issue",
                content: "Description of new issue",
                importance: 1,
                status: 1,
            };

            // STOMP 메시지 전송
            stompClient.publish({
                destination: '/projects/123/kanbanboards/' + kanbanBoardId,
                body: JSON.stringify(issueRequest),
                headers: { 'memberId': '789' } // memberId 등의 필요한 헤더
            });
            console.log('Message sent!');
        } else if(!stompClient) {
            console.log('Not connected to WebSocket');
        }else{
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