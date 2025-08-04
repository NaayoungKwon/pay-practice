package practice.common.dto

enum class ResponseCode(val message: String) {
    Success("성공"),
    Fail(""),
    InvalidUserId("유효하지 않은 사용자 ID"),
    InvalidMerchant("유효하지 않은 가맹점"),
    AlreadyOnGoing("해당 결제번호로 결제가 이미 진행 중"),
    AlreadyComplete("해당 결제번호로 결제 완료"),
    BankMaintenance("충전 계좌 점검"),
    NotEnoughAccountBalance("충전 계좌 잔고 부족"),
    ;
}
