import {Avatar, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";
import {useState} from "react";

const ChargerFilter = () => {
    const {projectMembers, setSelectedMember} = useKanbanBoardStore(state => ({
        projectMembers: state.projectMembers,
        setSelectedMember: state.setSelectedMember
    }));
    const [selectedMember, setSelectedMemberLocal] = useState('');

    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedMemberLocal(event.target.value);  // 로컬 상태 업데이트
        if (event.target.value == "") {
            setSelectedMember(null);  // Zustand 스토어 상태 업데이트
        } else {
            setSelectedMember(parseInt(event.target.value));  // Zustand 스토어 상태 업데이트
        }
    };

    return (
        <Box>
            {/** 담당자 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <InputLabel id="demo-simple-select-label" sx={{paddingTop: 0.5}}>담당자</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedMember}
                    label="담당자"
                    onChange={handleChange}
                    sx={{height: 49}}

                >
                    <MenuItem value="">
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90} height={32} >
                            <Typography> 선택해 주세요 </Typography>
                        </Box>
                    </MenuItem>
                    {projectMembers.map((member) => (
                        <MenuItem key={member.id} value={member.id}>
                            <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                                <Avatar sx={{width: 32, height: 32}} alt="프로필 이미지" src={member.profile}/>
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