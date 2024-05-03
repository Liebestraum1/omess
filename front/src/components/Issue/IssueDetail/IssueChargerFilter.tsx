import {
    Avatar,
    FormControl,
    MenuItem,
    outlinedInputClasses,
    Select,
    SelectChangeEvent,
    Typography
} from "@mui/material";
import Box from "@mui/material/Box";
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";
import {useEffect, useState} from "react";

type ChargerFilterPros = {
    id: number;
}

const ChargerFilter = ({id}  : ChargerFilterPros) => {
    const projectMembers = useKanbanBoardStore(state => state.projectMembers);
    const [selectedMember, setSelectedMember] = useState('');
    const handleChange = (event: SelectChangeEvent<string>) => {
        // FixMe 이슈 담당자 수정 api 호출
        setSelectedMember(event.target.value);
    };

    useEffect(() => {
        setSelectedMember(id.toString());
    }, [setSelectedMember]);


    return (
        <Box >
            {/** 담당자 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedMember}
                    onChange={handleChange}
                    sx={{
                        [`& .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // OutlinedInput 테두리 제거
                        [`&:hover .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 호버 시 테두리 제거
                        [`&.${outlinedInputClasses.focused} .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 포커스 시 테두리 제거
                    }}
                >
                    <MenuItem value="">
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}
                             height={32}>
                            <Typography> 선택해 주세요 </Typography>
                        </Box>
                    </MenuItem>
                    {projectMembers.map((member) => (
                        <MenuItem key={member.id} value={member.id}>
                            <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                                <Avatar sx={{ width: 32, height: 32 }} alt="프로필 이미지" src={member.profile} />
                                <Typography>{member.nickname}</Typography>
                            </Box>
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        </Box>
    );
}

export default ChargerFilter;