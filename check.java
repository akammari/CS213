public static boolean isCheckAndCheckmate(ArrayList<ReturnPiece> piecesOnBoard, Chess.Player currentPlayer) {
    // Find the king of the current player
    ReturnPiece king = findKing(piecesOnBoard, currentPlayer);

    // Check if the opponent's pieces can attack the king's position
    for (ReturnPiece opponentPiece : piecesOnBoard) {
        ReturnPiece.PieceType oppPieceType = opponentPiece.pieceType;
        if (opponentPiece.pieceType != king.pieceType
                && isPieceAttackingSquare(opponentPiece, king.pieceFile, king.pieceRank)) {
            // The king is in check

            // Check if there are any legal moves to get out of check
            if (!hasLegalMovesToEscapeCheck(piecesOnBoard, king, currentPlayer)) {
                // No legal moves to escape check, it's checkmate
                return true;
            }
        }
    }

    // The king is not in check or it has legal moves to escape
    return false;
}

private static boolean hasLegalMovesToEscapeCheck(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece king,
        Chess.Player currentPlayer) {
    // Iterate through all pieces of the current player
    for (ReturnPiece piece : piecesOnBoard) {
        if (piece.pieceType != king.pieceType && piece.pieceType != getOpponentPawnType(currentPlayer)) {
            // Consider only pieces other than the king and pawns for simplicity

            // Check all possible moves for each piece
            for (char file = 'a'; file <= 'h'; file++) {
                for (int rank = 1; rank <= 8; rank++) {
                    String destinationSquare = "" + file + rank;

                    // Check if the move is valid
                    if (isValidMove(piece, piece.pieceFile.toString() + piece.pieceRank, destinationSquare,
                            piecesOnBoard)) {
                        // Simulate the move
                        ReturnPiece movedPiece = new ReturnPiece(piece);
                        movedPiece.pieceFile = ReturnPiece.PieceFile.valueOf("" + file);
                        movedPiece.pieceRank = rank;
                        ArrayList<ReturnPiece> updatedBoard = updateBoardAfterMove(piecesOnBoard, piece,
                                piece.pieceFile.toString() + piece.pieceRank, destinationSquare);

                        // Check if the king is still in check after the move
                        if (!isCheck(updatedBoard, currentPlayer)) {
                            return true; // Found a legal move to escape check
                        }
                    }
                }
            }
        }
    }
    // No legal moves found to escape check
    return false;
}

private static boolean isValidMove(ReturnPiece piece, String sourceSquare, String destinationSquare,
        ArrayList<ReturnPiece> piecesOnBoard) {
    // Check if the move is valid for the given piece type
    switch (piece.pieceType) {
        case WP:
        case BP:
            return checkPawnMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        case WR:
        case BR:
            return checkRookMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        case WN:
        case BN:
            return checkKnightMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        case WB:
        case BB:
            return checkBishopMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        case WQ:
        case BQ:
            return checkQueenMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        case WK:
        case BK:
            return checkKingMove(sourceSquare, destinationSquare, piece, piecesOnBoard);
        default:
            return false; // Unknown piece type
    }
}
