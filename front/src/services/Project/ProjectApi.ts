import axios from "../common";

export const getProjectApi = async <T>(): Promise<T> => {
    const { data } = await axios.get<T>("/api/v1/projects");
    return data;
};
