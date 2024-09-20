'use client';

import {getChart, crawlChart, getChartOne} from "@/app/service/crawl/crawl.api";
import React, {useEffect, useState} from "react";

export default function CrawlMain() {

    const [error, setError] = useState<string | null>(null);
    const [chartList, setChartList] = useState<MusicChartModel[]>([]);

    const handleCrawl = () => {
        alert("벅스에서 뮤직차트를 가져옵니다.");
        crawl().then(getChart);
    };

    const crawl = async () => {
        try {
            const data = await crawlChart("https://music.bugs.co.kr/chart/track/realtime/total?wl_ref=M_contents_03_01");
            if (data.status === 500) {
                throw new Error("서버오류");
            }
        } catch (error) {
            setError("벅스 뮤직차트 크롤 중 오류 발생");
        }
    };


    const getChartList = async () => {
        try {
            const data = await getChart();
            console.log(data);
            if (data.status === 500) {
                throw new Error('서버 오류');
            }
            setChartList(data);
        } catch (error) {
            setError('뮤직차트 로드 중 오류 발생');
        }
    };

    const getOne = async () => {
        try {
            const data = await getChartOne();
            console.log(data);
            if (data.status === 500) {
                throw new Error('서버 오류');
            }
            setChartList(data);
        } catch (error) {
            setError('뮤직차트 로드 중 오류 발생');
        }
    };


    useEffect(() => {
        getChartList();
    }, []);

    return (
        <>
            <button onClick={handleCrawl}> crawl bugs music chart</button>
            <table className="table-auto">
                <thead>
                <tr className="border border-indigo-600">
                    <th>랭킹</th>
                    <th>곡명</th>
                    <th>아티스트</th>
                    <th>앨범</th>
                </tr>
                </thead>
                <tbody>
                {chartList.map((musicChart: MusicChartModel) => (
                    <tr key={musicChart.id}>

                        <td>{musicChart.ranking}</td>
                        <td>{musicChart.title}</td>
                        <td>{musicChart.artist}</td>
                        <td>{musicChart.album}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <nav aria-label="Page navigation example">
                <ul className="inline-flex -space-x-px text-sm">
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 ms-0 leading-tight text-gray-500 bg-white border border-e-0 border-gray-300 rounded-s-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">Previous</a>
                    </li>
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">1</a>
                    </li>
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">2</a>
                    </li>
                    <li>
                        <a href="#" aria-current="page"
                           className="flex items-center justify-center px-3 h-8 text-blue-600 border border-gray-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white">3</a>
                    </li>
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">4</a>
                    </li>
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">5</a>
                    </li>
                    <li>
                        <a href="#"
                           className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 rounded-e-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">Next</a>
                    </li>
                </ul>
            </nav>
        </>

    );
};