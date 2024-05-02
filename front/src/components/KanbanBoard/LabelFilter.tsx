import {Chip, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";
import {useState} from "react";

const LabelFilter = () => {
    const {labels, setSelectedLabel} = useKanbanBoardStore(state => ({
        labels: state.labels,
        setSelectedLabel: state.setSelectedLabel
    }));

    const [selectedLabel, setSelectedLabelLocal] = useState('');
    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedLabelLocal(event.target.value);
        if (event.target.value == "") {
            setSelectedLabel(null);
        } else {
            setSelectedLabel(parseInt(event.target.value));
        }
    };

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
                                <Chip label={label.name} color="primary" sx={{width: 80}}/>
                            </Box>
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

        </Box>
    );
}

export default LabelFilter;