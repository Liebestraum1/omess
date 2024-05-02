import { Autocomplete, Chip, TextField, Box } from "@mui/material";
import { useKanbanBoardStore } from "../../stores/KanbanBoardStorage.tsx";
import { useEffect, useState } from "react";

type IssueLabelFilterProp = {
    labelId: number;
}

const IssueLabelFilter = ({ labelId }: IssueLabelFilterProp) => {
    const labels = useKanbanBoardStore(state => state.labels);
    const [selectedLabel, setSelectedLabel] = useState(labels.find(label => label.labelId === labelId) || null);

    useEffect(() => {
        // labelId 업데이트 시 selectedLabel 상태 업데이트
        setSelectedLabel(labels.find(label => label.labelId === labelId) || null);
    }, [labelId, labels]);

    return (
        <Box>
            <Autocomplete
                id="label-select-demo"
                sx={{ width: 200 }}
                options={labels}
                autoHighlight
                getOptionLabel={(option) => option.name}
                value={selectedLabel}
                onChange={(_, newValue) => {
                    // FixMe 이슈 라벨 수정 api 호출
                    setSelectedLabel(newValue);
                }}
                isOptionEqualToValue={(option, value) => option.labelId === value?.labelId}
                renderOption={(props, option) => (
                    <Box component="li" {...props}>
                        <Chip label={option.name} color="primary" sx={{ width: 80 }}/>
                    </Box>
                )}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        InputProps={{
                            ...params.InputProps,
                            startAdornment: selectedLabel ? (
                                <Chip label={selectedLabel.name} color="primary" sx={{ width: 80 }}/>
                            ) : null,
                        }}
                        inputProps={{
                            ...params.inputProps,
                            // selectedLabel이 null이면 display 속성을 해제, 아니면 'none' 적용
                            style: selectedLabel ? { display: 'none' } : {},
                        }}
                    />
                )}
            />
        </Box>
    );
}

export default IssueLabelFilter;
