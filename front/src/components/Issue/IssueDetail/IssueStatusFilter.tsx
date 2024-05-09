import {
    FormControl,
    MenuItem,
    outlinedInputClasses,
    Select,
    SelectChangeEvent,
    Typography
} from "@mui/material";
import Box from "@mui/material/Box";
import {useEffect, useState} from "react";
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";

type IssueStatusFilterProp = {
    status: number;
}

const IssueStatusFilter = ({status}: IssueStatusFilterProp) => {

    const {kanbanBoardId, updateIssueStatus, issueId} = useKanbanBoardStore();
    const [selectedStatus, setSelectedStatus] = useState('');
    const handleChange = (event: SelectChangeEvent<string>) => {
        if (kanbanBoardId && issueId) {
            setSelectedStatus(event.target.value);

            updateIssueStatus(28, kanbanBoardId, issueId, parseInt(event.target.value))
        }
    };

    useEffect(() => {
        setSelectedStatus(status.toString());
    }, [status]);

    return (
        <Box>
            {/** 중요도 필터 */}
            <FormControl sx={{m: 1, minWidth: 120, paddingLeft: 3.3}} size={"small"}>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedStatus}
                    label="중요도"
                    onChange={handleChange}
                    sx={{
                        [`& .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // OutlinedInput 테두리 제거
                        [`&:hover .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 호버 시 테두리 제거
                        [`&.${outlinedInputClasses.focused} .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 포커스 시 테두리 제거
                    }}
                >
                    <MenuItem value="">
                        <Typography> 선택해 주세요 </Typography>
                    </MenuItem>
                    <MenuItem value={1}>
                        <Typography> 진행전 </Typography>
                    </MenuItem>
                    <MenuItem value={2}>
                        <Typography> 진행중 </Typography>
                    </MenuItem>
                    <MenuItem value={3}>
                        <Typography> 완료 </Typography>
                    </MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}

export default IssueStatusFilter;
