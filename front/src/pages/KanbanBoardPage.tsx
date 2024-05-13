import { Container } from "@mui/material";

import KanbanBoadrFilter from "../components/KanbanBoard/KanbanBoadrFilter.tsx";
import KanbanBoard from "../components/KanbanBoard/KanbanBoard.tsx";
import { useKanbanBoardStore } from "../stores/KanbanBoardStorage.tsx";
import { useEffect } from "react";
import { Stomp } from "@stomp/stompjs";

type KanbanBoardProps = {
    projectId: number;
    moduleId: number;
};


const KanbanBoardPage = ({ projectId, moduleId }: KanbanBoardProps) => {
    const {
        setClient,
        currentProjectId,
        setCurrentProjectId,
        kanbanBoardId,
        setKanbanBoardId,
        getKanbanBoard,
        getLabels,
        getMembersInproject
    } = useKanbanBoardStore();

    const serverUrl = import.meta.env.VITE_WEBSOCKET_URL;

    useEffect(() => {
        setKanbanBoardId(moduleId);
        setCurrentProjectId(projectId);

        if (kanbanBoardId && currentProjectId) {
            getKanbanBoard(currentProjectId, kanbanBoardId);
            getLabels(currentProjectId, kanbanBoardId);
            getMembersInproject(currentProjectId);
        }
    }, [moduleId, kanbanBoardId, projectId]);

    // stomp 칸반보드 구독
    useEffect(() => {
        const sock = new WebSocket(`wss://${serverUrl}/ws`);
        setClient(Stomp.over(() => sock));
    }, [kanbanBoardId]);

    return (
        <Container style={{ padding: 20 }}>
            <KanbanBoadrFilter />
            <KanbanBoard />
        </Container>
    );
};

export default KanbanBoardPage;
