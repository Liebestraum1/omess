import { useEffect, useRef } from "react";

const useFormListener = (listenTarget: string, logic: () => void, defaultLogic: () => void, timeLimit: number) => {
    const listenTargetTimeout = useRef<NodeJS.Timeout | null>(null);
    useEffect(() => {
        if (listenTarget != null && listenTarget != "") {
            if (listenTargetTimeout.current) {
                clearTimeout(listenTargetTimeout.current);
            }

            listenTargetTimeout.current = setTimeout(logic, timeLimit);
            return () => {
                if (listenTargetTimeout.current) {
                    clearTimeout(listenTargetTimeout.current);
                }
            };
        } else {
            defaultLogic();
        }
    }, [listenTarget]);
};

export default useFormListener;
