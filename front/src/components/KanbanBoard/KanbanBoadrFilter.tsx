import Box from "@mui/material/Box";
import {Container} from "@mui/material";
import ChargerFilter from "./ChargerFilter.tsx";
import LabelFilter from "./LabelFilter.tsx";
import ImpotanceFilter from "./ImpotanceFilter.tsx";

const KanbanBoadrFilter = () => {
    return (
        <Container >
            {/** 필터 */}
            <Box display="flex" sx={{ p: 0, paddingBottom: 1 }}> {/* 패딩 제거 */}
                <ChargerFilter />
                <LabelFilter />
                <ImpotanceFilter />
            </Box>
        </Container>
    );
}

export default KanbanBoadrFilter;