
const wish = 'http://localhost:8080/wish'

export async function toggleWish(amenity: WishModel, user: UserModel, product: ProductModel): Promise<any | { status: number }> {
    try {
        const param = new URLSearchParams({
            "userId": (user.id?? '').toString(),
            "productId": (product.id?? '').toString()
            })

        const response = await fetch(`http://localhost:8080/wish?${param}`, {
            method: 'GET'
        });

        const data: any = await response.json();

        return data;

    } catch (error) {
        console.error("위시 상태 변경 중 오류 발생", error);

        return {status: 500}
    }
}

export async function selectWishList(user: UserModel) {
    try {
        const response = await fetch('http://localhost:8080/wish/' + user.id, {
            method: 'GET'
        });

        const data = await response.json();

        console.log("+++++>" + JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("위시 리스트 로드 중 오류 발생", error);
        return { status: 500 };
    }
}


export async function deleteWish(wish: WishModel) {
    try {

        const response = await fetch('http://localhost:8080/wish/' + wish.id, {
            method: 'DELETE'
        });

        const data: any = await response;

        return data;

    } catch (error) {
        console.error("위시 리스트 삭제 중 오류 발생", error);

        return {status: 500}
    }

}