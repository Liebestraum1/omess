import {Chip, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";
import {useEffect, useState} from "react";
import {ColorArr} from "../../services/common/Color.ts";

const LabelFilter = () => {
    const {labels, setSelectedLabel} = useKanbanBoardStore(state => ({
        labels: state.labels,
        setSelectedLabel: state.setSelectedLabel
    }));

    const [selectedLabel, setSelectedLabelLocal] = useState('');
    const [colorArr, setColorArr] = useState<string[]>([]);

    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedLabelLocal(event.target.value);
        if (event.target.value == "") {
            setSelectedLabel(null);
        } else {
            setSelectedLabel(parseInt(event.target.value));
        }
    };

    useEffect(() => {
        setColorArr(ColorArr);
    }, [setColorArr]);


    return (
        <Box>
            {/** 라벨 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <InputLabel id="demo-simple-select-label" sx={{paddingTop: 0.5}}>라벨</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedLabel}
                    label="라벨"
                    onChange={handleChange}
                    sx={{height: 49}}
                >
                    <MenuItem value="">
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                            <Typography> 선택해 주세요 </Typography>
                        </Box>
                    </MenuItem>
                    {labels.map((label) => (
                        <MenuItem key={label.labelId} value={label.labelId}>
                            <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                                <Chip label={label.name} sx={{
                                    width: 80,
                                    borderColor: colorArr[label.labelId % 20], // 테두리 색상
                                    backgroundColor: colorArr[label.labelId % 20],
                                    '&:hover': {
                                        backgroundColor: colorArr[label.labelId % 20], // 호버 시 배경 색상 추가
                                        color: 'white' // 호버 시 글자 색상 변경
                                    }
                                }} />
                            </Box>
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

        </Box>
    );
}

export default LabelFilter;