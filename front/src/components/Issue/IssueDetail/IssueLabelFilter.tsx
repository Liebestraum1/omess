import {
    Chip,
    Box,
    FormControl,
    Select,
    MenuItem,
    Typography,
    SelectChangeEvent,
    outlinedInputClasses
} from "@mui/material";
import {useKanbanBoardStore} from "../../../stores/KanbanBoardStorage.tsx";
import {useEffect, useState} from "react";
import {ColorArr} from "../../../services/common/Color.ts";
import {LabelProp} from "../../../types/Label/Label.ts";

type IssueLabelFilterProp = {
    labelId: number | null;
}

const IssueLabelFilter = ({labelId}: IssueLabelFilterProp) => {

    const {kanbanBoardId, labels, updateIssueLabel, issueId} = useKanbanBoardStore();
    const [selectedLabel, setSelectedLabel] = useState("");
    const [colorArr, setColorArr] = useState<string[]>([]);

    const handleChange = (event: SelectChangeEvent<string>) => {
        if (kanbanBoardId && issueId) {
            setSelectedLabel(event.target.value);

            updateIssueLabel(28, kanbanBoardId, issueId, parseInt(event.target.value))
        }
    };

    useEffect(() => {
        setSelectedLabel(labelId ? labelId.toString() : "0");
    }, [labelId]);


    useEffect(() => {
        setColorArr(ColorArr);
    }, [setColorArr]);


    return (
        <Box>
            {/** 라벨 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedLabel}
                    onChange={handleChange}
                    sx={{
                        [`& .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // OutlinedInput 테두리 제거
                        [`&:hover .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 호버 시 테두리 제거
                        [`&.${outlinedInputClasses.focused} .${outlinedInputClasses.notchedOutline}`]: {border: 'none'}, // 포커스 시 테두리 제거
                    }}
                >
                    <MenuItem value={0}>
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={100}>
                            <Typography> 선택해 주세요 </Typography>
                        </Box>
                    </MenuItem>
                    {labels && labels.map((label: LabelProp) => {
                        return (
                            <MenuItem key={label.labelId} value={label.labelId}>
                                <Box display={"flex"} justifyContent="space-between" alignItems="center" width={100}>
                                    <Chip label={label.name} sx={{
                                        width: 80,
                                        borderColor: colorArr[label.labelId % 20], // 테두리 색상
                                        backgroundColor: colorArr[label.labelId % 20],
                                        '&:hover': {
                                            backgroundColor: colorArr[label.labelId % 20], // 호버 시 배경 색상 추가
                                            color: 'white' // 호버 시 글자 색상 변경
                                        }
                                    }}/>
                                </Box>
                            </MenuItem>);
                    })}
                </Select>
            </FormControl>

        </Box>
    );
}

export default IssueLabelFilter;
