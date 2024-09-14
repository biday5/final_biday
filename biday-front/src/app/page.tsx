"use client";
import React, { useEffect, useState } from "react";
import { deleteWish, selectWishList } from "@/app/service/wish/wish.api";
import { crawlChart} from "@/app/service/crawl/crawl.api";

export default function Home() {
    const [wishList, setWishList] = useState<WishModel[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [selectedIds, setSelectedIds] = useState<number[]>([]);


    const user: UserModel = {
        id: 1
    };

    const moveToSave = () => {
        alert("등록 페이지로 이동합니다.");
        window.location.href = "/shull/wish/insert";
    };

    const loadWishList = async () => {
        try {
            const data = await selectWishList(user);
            console.log(data);
            if (data.status === 500) {
                throw new Error('서버 오류');
            }
            setWishList(data);
        } catch (error) {
            setError('위시리스트 로드 중 오류 발생');
        }
    };

    const handleCheckboxChange = (wishId: number) => {
        if (selectedIds.includes(wishId)) {
            setSelectedIds(selectedIds.filter(id => id !== wishId));
        } else {
            setSelectedIds([...selectedIds, wishId]);
        }
    };

    const handleDelete = async () => {
        if (selectedIds.length === 0) {
            alert("삭제할 항목을 선택해주세요.");
            return;
        }

        try {
            for (const id of selectedIds) {
                const wishToDelete = wishList.find(wish => wish.id === id);
                if (wishToDelete) {
                    const response = await deleteWish(wishToDelete);
                    if (response.status === 500) {
                        throw new Error('삭제 중 서버 오류 발생');
                    }
                }
            }

            const updatedWishList = wishList.filter((wish) => {
                if (wish.id != null) {
                    !selectedIds.includes(wish.id)
                }
            });
            setWishList(updatedWishList);
            setSelectedIds([]);
            alert("선택한 항목이 삭제되었습니다.");
        } catch (error) {
            console.error(error);
            setError("위시 리스트 삭제 중 오류 발생");
        }
    };

    const moveToCrawl = () => {
        alert("크롤 페이지로 이동합니다.");
        window.location.href = "/shull/crawl/main";
    };

    useEffect(() => {
        loadWishList();
    }, []);

    return (
        <main className="flex min-h-screen flex-col items-center justify-between p-24">
            <table className="table-auto">
                <thead>
                <tr className="border border-indigo-600">
                    <th>선택</th>
                    <th>id</th>
                    <th>userId</th>
                    <th>productId</th>
                </tr>
                </thead>
                <tbody>
                {wishList?.map((wish: WishModel) => (
                    <tr key={wish.id}>
                        <td>
                            <input
                                type="checkbox"
                                checked={selectedIds.includes(wish.id as number)}
                                onChange={() => handleCheckboxChange(wish.id as number)}
                            />
                        </td>
                        <td>{wish.id}</td>
                        <td>{wish.user.id}</td>
                        <td>{wish.product.id}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <button className="rounded-full bg-red-500 text-white p-2 mt-4" onClick={handleDelete}>
                Delete Selected
            </button>

            <button className="rounded-full bg-blue-500 text-white p-2 mt-4" onClick={moveToSave}>
                Move To Save
            </button>
        </main>
    );
}