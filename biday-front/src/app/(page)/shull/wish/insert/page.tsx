"use client";

import {useState} from "react";
import {useRouter} from "next/router";

export function WishInsert() {

    const [productId, setProductId] = useState<string>('');
    const [userId, setUserId] = useState<string>('');
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // API에 데이터를 POST로 전송
        const res = await fetch('/api/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                productId,
                userId,
            }),
        });

        if (res.ok) {
            // API 호출이 성공하면 메인 페이지로 이동
            router.push('/');
        } else {
            console.error('API 호출 실패');
        }
    };

    return (
        <div>
            <h1>Submit Product and User</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Product:
                    <select value={productId} onChange={(e) => setProductId(e.target.value)}>
                        <option value="">Select Product</option>
                        <option value="1">Product 1</option>
                        <option value="2">Product 2</option>
                        <option value="3">Product 3</option>
                    </select>
                </label>

                <label>
                    User:
                    <select value={userId} onChange={(e) => setUserId(e.target.value)}>
                        <option value="">Select User</option>
                        <option value="100">User 100</option>
                        <option value="101">User 101</option>
                        <option value="102">User 102</option>
                    </select>
                </label>

                <button type="submit">Submit</button>
            </form>
        </div>
    );
};

export default WishInsert;