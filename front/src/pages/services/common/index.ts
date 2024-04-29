import axios from 'axios'

const baseURL = "http://localhost:8080"

const client = axios.create({
    baseURL
});

export const get = async<T> (url: string): Promise<T> => {
    const {data} = await client.get<T>(url);
    return data;
}

export default client;