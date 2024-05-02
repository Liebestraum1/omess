import {
    Container,
} from "@mui/material";

import KanbanBoadrFilter from "../components/KanbanBoard/KanbanBoadrFilter.tsx";
import KanbanBoard from "../components/KanbanBoard/KanbanBoard.tsx";
import {LabelProp} from "../types/Label/Label.ts";
import {useKanbanBoardStore} from "../stores/KanbanBoardStorage.tsx";
import {MemberProp} from "../types/Member/Member.ts";
import {useEffect} from "react";

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

const KanbanBoardPage = () => {


    const setLabels = useKanbanBoardStore(state => state.setLabels);
    const setProjectMembers = useKanbanBoardStore(state => state.setprojectMembers);
    const selectedLabel = useKanbanBoardStore(state => state.selectedLabel);
    const selectedMember = useKanbanBoardStore(state => state.selectedMember);
    const selectedImpotance = useKanbanBoardStore(state => state.selectedImpotance);
    
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
        setProjectMembers(projectMembers);
    }, [setLabels, setProjectMembers]); // 의존성 배열에 추가

    return (
        <Container style={{padding: 20}}>
            <KanbanBoadrFilter/>
            <KanbanBoard/>

        </Container>
    );
}

export default KanbanBoardPage;