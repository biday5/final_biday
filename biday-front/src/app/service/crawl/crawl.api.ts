
export async function crawlChart(url: String): Promise<any | { status: number }> {

    try {

        const response = await fetch(`http://localhost:8080/selenium/crawl?url=${url}`, {
            method: 'GET'
        });

        const data: any = await response.json();

        return data;

    } catch (error) {
        console.error("뮤직 차트 크롤 중 오류 발생", error);

        return {status: 500}
    }
}

export async function getChart(): Promise<any | { status: number}> {
    try {
        const response = await fetch('http://localhost:8080/selenium', {
            method: 'GET'
        });

        if (!response.ok) {
            console.error("서버 응답 오류:", response.status);
            return { status: response.status };
        }

        const data = await response.json();

        console.log(JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("뮤직 차트 로드 중 오류 발생", error);
        return { status: 500 };
    }
}

export async function getChartOne() {
    try {
        const response = await fetch('http://localhost:8080/selenium/1', {
            method: 'GET'
        });

        if (!response.ok) {
            console.error("서버 응답 오류:", response.status);
            return { status: response.status };
        }

        const data = await response.json();

        console.log(JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("뮤직 차트 로드 중 오류 발생", error);
        return { status: 500 };
    }
}

