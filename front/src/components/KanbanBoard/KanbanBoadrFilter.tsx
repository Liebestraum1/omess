import Box from "@mui/material/Box";
import {Button, Container} from "@mui/material";
import ChargerFilter from "./ChargerFilter.tsx";
import LabelFilter from "./LabelFilter.tsx";
import ImpotanceFilter from "./ImpotanceFilter.tsx";
import {useState} from "react";
import LabelManageModal from "../label/LabelManageModal.tsx";

const KanbanBoadrFilter = () => {
    const [openLabelModal, setOpenLabelModal] = useState(false);
    const handlOpenLabel = () => {
        setOpenLabelModal(true);
    }

    const handleCloseLabel = () => {
        setOpenLabelModal(false);
    }

    return (
        <Container>
            {/** 필터 */}
            <Box display="flex" justifyContent={"space-between"} sx={{p: 0, paddingBottom: 1}}> {/* 패딩 제거 */}
                <Box display="flex">
                    <ChargerFilter/>
                    <LabelFilter/>
                    <ImpotanceFilter/>
                </Box>
                <Box paddingY={1}>
                    <Button
                        sx={{
                            width: 120,
                            height: 50,
                            borderColor: 'gray', // 테두리 색상 설정
                            borderWidth: 1, // 테두리 두께
                            borderStyle: 'solid', // 테두리 스타일
                            color: 'gray', // 버튼의 텍스트 색상
                        }}

                        onClick={handlOpenLabel}
                    >
                        라벨
                    </Button>
                    <LabelManageModal open={openLabelModal} onClose={handleCloseLabel}/>
                </Box>
            </Box>
        </Container>
    );
}

export default KanbanBoadrFilter;