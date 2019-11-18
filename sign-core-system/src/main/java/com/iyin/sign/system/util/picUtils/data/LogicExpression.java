package com.iyin.sign.system.util.picUtils.data;

import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.To;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class LogicExpression extends Expression
/*     */ {
    /*     */   private static final int OPERATOR_TYPE_OR = 1;
    /*     */   private static final int OPERATOR_TYPE_AND = 2;
    /*     */   private static final int OPERATOR_TYPE_NOT = 3;
    /*     */   public static final int OPERATOR_TYPE_EQUAL = 4;
    /*     */   public static final int OPERATOR_TYPE_NOTEQUAL = 5;
    /*     */   public static final int OPERATOR_TYPE_MORE = 6;
    /*     */   public static final int OPERATOR_TYPE_MOREEQUAL = 7;
    /*     */   public static final int OPERATOR_TYPE_LESS = 8;
    /*     */   public static final int OPERATOR_TYPE_LESSEQUAL = 9;
    /*     */   private static final int OPERATOR_TYPE_IN = 10;
    /*     */   private static final int OPERATOR_TYPE_NOTIN = 11;
    /*     */   private static final int OPERATOR_TYPE_LIKE = 12;
    /*     */   private static final int OPERATOR_TYPE_NOTLIKE = 13;
    /*     */   private static final int OPERATOR_TYPE_RLIKE = 14;
    /*     */   private static final int OPERATOR_TYPE_NOTRLIKE = 15;
    /*     */   private static final int OPERATOR_TYPE_EXISTS = 16;
    /*     */   private static final int OPERATOR_TYPE_BETWEEN = 17;
    /*     */   private static final int PARSE_RES_TYPE_NONE = 0;
    /*     */   private static final int PARSE_RES_TYPE_RELATION_EXPRESSION = 1;
    /*     */   private static final int PARSE_RES_TYPE_LOGIC_EXPRESSION = 2;
    /*     */   private static final int PARSE_RES_TYPE_LOGIC_RATION_EXPRESSION = 3;
    /*     */   private static final int PARSE_RES_TYPE_MATH_EXPRESSION = 4;
    /*     */   private static final int RATION_SOME = 1;
    /*     */   private static final int RATION_ALL = 2;
    /*     */   private int ration;
    /* 132 */   public int parseResType = 0;
    /*     */   private Expression expA;
    /*     */   private Expression expB;
    /*     */   private int operator;

    /*     */
/*     */
    public LogicExpression(BlkExpression blkExp, String str)
/*     */ {
/* 149 */
        super(blkExp, str);
/* 150 */
        this.resultDataType = 16;
/*     */
    }

    /*     */
/*     */
    public void parse()
/*     */     throws HgException
/*     */ {
/* 157 */
        if (this.expStr.length() > 0)
/*     */ {
/* 160 */
            boolean strBegin = false;
/*     */ 
/* 162 */
            boolean singleQuotBegin = false;
/*     */ 
/* 164 */
            int pars = 0;
/*     */ 
/* 166 */
            String expStrA = "";
/*     */ 
/* 168 */
            String expStrB = "";
/*     */ 
/* 170 */
            String tmpExpStr = this.expStr.trim();
/*     */ 
/* 172 */
            boolean split = false;
/*     */ 
/* 174 */
            boolean betweenBegin = false;
/*     */
            while (true)
/*     */ {
/* 177 */
                for (int i = 0; i < tmpExpStr.length(); i++) {
/* 178 */
                    char c = tmpExpStr.charAt(i);
/* 179 */
                    if ((c != '\'') && (singleQuotBegin)) {
/* 180 */
                        strBegin = false;
/* 181 */
                        singleQuotBegin = false;
/*     */
                    }
/* 183 */
                    if (c == '\'') {
/* 184 */
                        if (!strBegin) {
/* 185 */
                            strBegin = true;
/*     */
                        }
/* 187 */
                        else if (singleQuotBegin) {
/* 188 */
                            singleQuotBegin = false;
/*     */
                        }
/*     */
                        else {
/* 191 */
                            singleQuotBegin = true;
/*     */
                        }
/*     */
                    }
/* 194 */
                    else if ((!strBegin) && (c == '('))
/* 195 */ pars++;
/* 196 */
                    else if ((!strBegin) && (c == ')')) {
/* 197 */
                        pars--;
/*     */
                    }
/* 199 */
                    if ((!strBegin) && (pars == 0) && (tmpExpStr.length() > i + 9) &&
/* 200 */             (tmpExpStr.substring(i, i + 9).equals(" BETWEEN "))) {
/* 201 */
                        betweenBegin = true;
/* 202 */
                        i += 8;
/*     */
                    }
/* 204 */
                    if ((strBegin) || (pars != 0) || (!betweenBegin) || (tmpExpStr.length() <= i + 5) ||
/* 205 */             (!tmpExpStr.substring(i, i + 5).equals(" AND ")))
/*     */ continue;
/* 207 */
                    tmpExpStr = tmpExpStr.substring(0, i) + "  ,  " + tmpExpStr.substring(i + 5);
/* 208 */
                    betweenBegin = false;
/* 209 */
                    i += 4;
/*     */
                }
/*     */ 
/* 213 */
                for (int i = tmpExpStr.length() - 1; i > -1; i--) {
/* 214 */
                    char c = tmpExpStr.charAt(i);
/* 215 */
                    if ((c != '\'') && (singleQuotBegin)) {
/* 216 */
                        strBegin = false;
/* 217 */
                        singleQuotBegin = false;
/*     */
                    }
/* 219 */
                    if (c == '\'') {
/* 220 */
                        if (!strBegin) {
/* 221 */
                            strBegin = true;
/*     */
                        }
/* 223 */
                        else if (singleQuotBegin) {
/* 224 */
                            singleQuotBegin = false;
/*     */
                        }
/*     */
                        else {
/* 227 */
                            singleQuotBegin = true;
/*     */
                        }
/*     */
                    }
/* 230 */
                    else if ((!strBegin) && (c == '('))
/* 231 */ pars++;
/* 232 */
                    else if ((!strBegin) && (c == ')')) {
/* 233 */
                        pars--;
/*     */
                    }
/* 235 */
                    if ((strBegin) || (pars != 0) || (i - 3 <= -1) ||
/* 236 */             (!tmpExpStr.substring(i - 3, i + 1).equals(" OR "))) continue;
/* 237 */
                    this.operator = 1;
/* 238 */
                    expStrA = tmpExpStr.substring(0, i - 3);
/* 239 */
                    expStrB = tmpExpStr.substring(i + 1);
/* 240 */
                    split = true;
/* 241 */
                    break;
/*     */
                }
/*     */ 
/* 244 */
                if (!split) {
/* 245 */
                    for (int i = tmpExpStr.length() - 1; i > -1; i--) {
/* 246 */
                        char c = tmpExpStr.charAt(i);
/* 247 */
                        if ((c != '\'') && (singleQuotBegin)) {
/* 248 */
                            strBegin = false;
/* 249 */
                            singleQuotBegin = false;
/*     */
                        }
/* 251 */
                        if (c == '\'') {
/* 252 */
                            if (!strBegin) {
/* 253 */
                                strBegin = true;
/*     */
                            }
/* 255 */
                            else if (singleQuotBegin) {
/* 256 */
                                singleQuotBegin = false;
/*     */
                            }
/*     */
                            else {
/* 259 */
                                singleQuotBegin = true;
/*     */
                            }
/*     */
                        }
/* 262 */
                        else if ((!strBegin) && (c == '(')) {
/* 263 */
                            pars++;
/* 264 */
                        } else if ((!strBegin) && (c == ')')) {
/* 265 */
                            pars--;
                        } else {
/* 266 */
                            if ((strBegin) || (pars != 0) || (i - 4 <= -1) ||
/* 267 */                 (!tmpExpStr.substring(i - 4, i + 1).equals(" AND "))) continue;
/* 268 */
                            this.operator = 2;
/* 269 */
                            expStrA = tmpExpStr.substring(0, i - 4);
/* 270 */
                            expStrB = tmpExpStr.substring(i + 1);
/* 271 */
                            split = true;
/* 272 */
                            break;
/*     */
                        }
/*     */
                    }
/*     */
                }
/* 276 */
                if ((!split) &&
/* 277 */           (tmpExpStr.length() > 4) && (tmpExpStr.substring(0, 4).equals("NOT ")))
/*     */ {
/* 279 */
                    this.operator = 3;
/* 280 */
                    expStrA = tmpExpStr.substring(4, tmpExpStr.length());
/* 281 */
                    split = true;
/*     */
                }
/*     */ 
/* 284 */
                if (!split) {
/* 285 */
                    for (int i = 0; i < tmpExpStr.length(); i++) {
/* 286 */
                        char c = tmpExpStr.charAt(i);
/* 287 */
                        if ((c != '\'') && (singleQuotBegin)) {
/* 288 */
                            strBegin = false;
/* 289 */
                            singleQuotBegin = false;
/*     */
                        }
/* 291 */
                        if (c == '\'') {
/* 292 */
                            if (!strBegin) {
/* 293 */
                                strBegin = true;
/*     */
                            }
/* 295 */
                            else if (singleQuotBegin) {
/* 296 */
                                singleQuotBegin = false;
/*     */
                            }
/*     */
                            else {
/* 299 */
                                singleQuotBegin = true;
/*     */
                            }
/*     */
                        }
/* 302 */
                        else if ((!strBegin) && (c == '('))
/* 303 */ pars++;
/* 304 */
                        else if ((!strBegin) && (c == ')')) {
/* 305 */
                            pars--;
/*     */
                        }
/* 307 */
                        if ((!strBegin) && (pars == 0)) {
/* 308 */
                            if ((tmpExpStr.length() > i + 2) &&
/* 309 */                 (tmpExpStr.substring(i, i + 2).equals(">="))) {
/* 310 */
                                this.operator = 7;
/* 311 */
                                expStrA = tmpExpStr.substring(0, i);
/* 312 */
                                expStrB = tmpExpStr.substring(i + 2);
/* 313 */
                                split = true;
/* 314 */
                                break;
/* 315 */
                            }
                            if ((tmpExpStr.length() > i + 2) && (
/* 316 */                 (tmpExpStr.substring(i, i + 2).equals("<>")) || 
/* 317 */                 (tmpExpStr.substring(i, i + 2).equals("!=")))) {
/* 318 */
                                this.operator = 5;
/* 319 */
                                expStrA = tmpExpStr.substring(0, i);
/* 320 */
                                expStrB = tmpExpStr.substring(i + 2);
/* 321 */
                                split = true;
/* 322 */
                                break;
/* 323 */
                            }
                            if ((tmpExpStr.length() > i + 2) &&
/* 324 */                 (tmpExpStr.substring(i, i + 2).equals("<="))) {
/* 325 */
                                this.operator = 9;
/* 326 */
                                expStrA = tmpExpStr.substring(0, i);
/* 327 */
                                expStrB = tmpExpStr.substring(i + 2);
/* 328 */
                                split = true;
/* 329 */
                                break;
/* 330 */
                            }
                            if ((tmpExpStr.length() > i + 1) &&
/* 331 */                 (tmpExpStr.substring(i, i + 1).equals(">"))) {
/* 332 */
                                this.operator = 6;
/* 333 */
                                expStrA = tmpExpStr.substring(0, i);
/* 334 */
                                expStrB = tmpExpStr.substring(i + 1);
/* 335 */
                                split = true;
/* 336 */
                                break;
/* 337 */
                            }
                            if ((tmpExpStr.length() > i + 1) &&
/* 338 */                 (tmpExpStr.substring(i, i + 1).equals("<"))) {
/* 339 */
                                this.operator = 8;
/* 340 */
                                expStrA = tmpExpStr.substring(0, i);
/* 341 */
                                expStrB = tmpExpStr.substring(i + 1);
/* 342 */
                                split = true;
/* 343 */
                                break;
/* 344 */
                            }
                            if ((tmpExpStr.length() > i + 1) &&
/* 345 */                 (tmpExpStr.substring(i, i + 1).equals("="))) {
/* 346 */
                                this.operator = 4;
/* 347 */
                                expStrA = tmpExpStr.substring(0, i);
/* 348 */
                                expStrB = tmpExpStr.substring(i + 1);
/* 349 */
                                split = true;
/* 350 */
                                break;
/* 351 */
                            }
                            if ((tmpExpStr.length() > i + 4) &&
/* 352 */                 (tmpExpStr.substring(i, i + 4).equals(" IS "))) {
/* 353 */
                                expStrA = tmpExpStr.substring(0, i);
/* 354 */
                                expStrB = tmpExpStr.substring(i + 4).trim();
/* 355 */
                                if (!expStrB.startsWith("NOT ")) {
/* 356 */
                                    this.operator = 4;
/*     */
                                } else {
/* 358 */
                                    expStrB = expStrB.substring(4);
/* 359 */
                                    this.operator = 5;
/*     */
                                }
/* 361 */
                                split = true;
/* 362 */
                                break;
/* 363 */
                            }
                            if ((tmpExpStr.length() > i + 4) &&
/* 364 */                 (tmpExpStr.substring(i, i + 4).equals(" IN "))) {
/* 365 */
                                expStrA = tmpExpStr.substring(0, i).trim();
/* 366 */
                                expStrB = tmpExpStr.substring(i + 4);
/* 367 */
                                if (expStrA.endsWith(" NOT")) {
/* 368 */
                                    this.operator = 11;
/* 369 */
                                    expStrA = expStrA.substring(0, expStrA.length() - 4);
/*     */
                                } else {
/* 371 */
                                    this.operator = 10;
/*     */
                                }
/* 373 */
                                split = true;
/* 374 */
                                break;
/* 375 */
                            }
                            if ((tmpExpStr.length() > i + 6) &&
/* 376 */                 (tmpExpStr.substring(i, i + 6).equals(" LIKE "))) {
/* 377 */
                                expStrA = tmpExpStr.substring(0, i).trim();
/* 378 */
                                expStrB = tmpExpStr.substring(i + 6);
/* 379 */
                                if (expStrA.endsWith(" NOT")) {
/* 380 */
                                    this.operator = 13;
/* 381 */
                                    expStrA = expStrA.substring(0, expStrA.length() - 4);
/*     */
                                } else {
/* 383 */
                                    this.operator = 12;
/*     */
                                }
/* 385 */
                                split = true;
/* 386 */
                                break;
/* 387 */
                            }
                            if ((tmpExpStr.length() > i + 7) &&
/* 388 */                 (tmpExpStr.substring(i, i + 7).equals(" RLIKE "))) {
/* 389 */
                                expStrA = tmpExpStr.substring(0, i).trim();
/* 390 */
                                expStrB = tmpExpStr.substring(i + 7);
/* 391 */
                                split = true;
/* 392 */
                                if (expStrA.endsWith(" NOT")) {
/* 393 */
                                    this.operator = 15;
/* 394 */
                                    expStrA = expStrA.substring(0, expStrA.length() - 4);
/* 395 */
                                    break;
/* 396 */
                                }
                                this.operator = 14;
/*     */ 
/* 398 */
                                break;
/* 399 */
                            }
                            if ((tmpExpStr.length() <= i + 9) ||
/* 400 */                 (!tmpExpStr.substring(i, i + 9).equals(" BETWEEN "))) continue;
/* 401 */
                            expStrA = tmpExpStr.substring(0, i);
/* 402 */
                            expStrB = tmpExpStr.substring(i + 9);
/* 403 */
                            this.operator = 17;
/* 404 */
                            split = true;
/* 405 */
                            break;
/*     */
                        }
/*     */
                    }
/*     */
                }
/*     */ 
/* 410 */
                if (split) break;
/* 411 */
                if ((tmpExpStr.charAt(0) == '(') && (tmpExpStr.charAt(tmpExpStr.length() - 1) == ')'))
/*     */ {
/* 413 */
                    tmpExpStr = tmpExpStr.substring(1, tmpExpStr.length() - 1);
/* 414 */
                    continue;
                }
                if ((tmpExpStr.length() <= 7) || (!tmpExpStr.substring(0, 7).equals("EXISTS "))) break;
/* 415 */
                this.operator = 16;
/* 416 */
                expStrA = tmpExpStr.substring(7, tmpExpStr.length());
/* 417 */
                split = true;
/*     */
            }
/*     */ 
/* 427 */
            if (split) {
/* 428 */
                if ((this.operator == 2) || (this.operator == 1)) {
/* 429 */
                    this.expA = new LogicExpression(this.blkExp, expStrA);
/* 430 */
                    this.expB = new LogicExpression(this.blkExp, expStrB);
/* 431 */
                    this.parseResType = 1;
/* 432 */
                } else if (this.operator == 3) {
/* 433 */
                    this.expA = new LogicExpression(this.blkExp, expStrA);
/* 434 */
                    this.parseResType = 1;
/* 435 */
                } else if (this.operator == 17)
/*     */ {
/* 437 */
                    String[] strs = Parser.split(expStrB, ',');
/* 438 */
                    this.expA = new LogicExpression(this.blkExp, expStrA + ">=" + strs[0]);
/* 439 */
                    this.expB = new LogicExpression(this.blkExp, expStrA + "<=" + strs[1]);
/* 440 */
                    this.operator = 2;
/* 441 */
                    this.parseResType = 1;
/*     */
                } else {
/* 443 */
                    expStrB = expStrB.trim();
/* 444 */
                    this.expB = new MathExpression(this.blkExp, expStrB);
/* 445 */
                    this.parseResType = 2;
/* 446 */
                    this.expA = new MathExpression(this.blkExp, expStrA);
/*     */
                }
/* 448 */
                this.expA.parse();
/* 449 */
                if (this.expB != null)
/* 450 */ this.expB.parse();
/*     */
            }
/*     */
            else {
/* 453 */
                this.parseResType = 4;
/* 454 */
                this.expA = new MathExpression(this.blkExp, tmpExpStr);
/* 455 */
                this.expA.parse();
/*     */
            }
/*     */
        } else {
/* 458 */
            this.parseResType = 4;
/* 459 */
            this.expA = new MathExpression(this.blkExp, "TRUE");
/* 460 */
            this.expA.parse();
/*     */
        }
/* 462 */
        parseEval();
/* 463 */
        this.parsed = true;
/*     */
    }

    /*     */
    private void evalExpB(Conn conn) throws HgException {
/* 466 */
        if (this.expB != null) {
/* 467 */
            this.expB.data = this.data;
/* 468 */
            this.expB.dataStyle = this.dataStyle;
/* 469 */
            this.expB.eval(conn);
/* 470 */
            if (this.expB.resultDataStyle == 4)
/* 471 */ ((MathExpression) this.expB).compress();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public void parseEval()
/*     */     throws HgException
/*     */ {
/* 480 */
        this.parseRes = null;
/* 481 */
        if (this.expA != null) this.expA.parse();
/* 482 */
        if (this.expB != null) this.expB.parse();
/* 483 */
        if ((this.parseResType == 4) &&
/* 484 */       (((MathExpression) this.expA).isConst())) {
/* 485 */
            this.parseRes = new Boolean(To.toBool(this.expA.parseRes));
/* 486 */
        } else if (this.operator == 2) {
/* 487 */
            if ((this.expA.parseRes != null) && (!To.toBool(this.expA.parseRes)))
/*     */ {
/* 489 */
                this.parseRes = Boolean.FALSE;
/* 490 */
            } else if ((this.expB.parseRes != null) && (!To.toBool(this.expB.parseRes)))
/*     */ {
/* 492 */
                this.parseRes = Boolean.FALSE;
/*     */
            }
/* 494 */
        } else if (this.operator == 1) {
/* 495 */
            if ((this.expA.parseRes != null) && (To.toBool(this.expA.parseRes)))
/*     */ {
/* 497 */
                this.parseRes = Boolean.TRUE;
/* 498 */
            } else if ((this.expB.parseRes != null) && (To.toBool(this.expB.parseRes)))
/*     */ {
/* 500 */
                this.parseRes = Boolean.TRUE;
/*     */
            }
/* 502 */
        } else if (this.operator == 3) {
/* 503 */
            if ((this.expA.parseRes != null) && (To.toBool(this.expA.parseRes)))
/* 504 */ this.parseRes = Boolean.FALSE;
/*     */
        }
/* 506 */
        else if ((this.operator != 16) &&
/* 507 */       (this.parseResType != 3) && 
/* 508 */       (((MathExpression) this.expA).isConst()) && (((MathExpression) this.expB).isConst())) {
/* 509 */
            int type = getCompareType();
/* 510 */
            int n = 0;
/* 511 */
            if ((this.operator != 10) && (this.operator != 11)) {
/* 512 */
                n = ObjectUtil.compare(this.expA.parseRes, this.expB.parseRes, type);
/* 513 */
                this.parseRes = Boolean.FALSE;
/*     */
            }
/* 515 */
            if (((this.operator == 7) && (n >= 0)) ||
/* 516 */         ((this.operator == 6) && (n > 0)) || 
/* 517 */         ((this.operator == 4) && (n == 0)) || 
/* 518 */         ((this.operator == 5) && (n != 0)) || 
/* 519 */         ((this.operator == 9) && (n <= 0)) || (
/* 520 */         (this.operator == 8) && (n < 0))) {
/* 521 */
                this.parseRes = Boolean.TRUE;
/* 522 */
            } else if (this.operator == 12)
/*     */ {
/* 524 */
                if (mCharMatch(To.objToString(this.expA.parseRes),
/* 524 */           To.objToString(this.expB.parseRes)))
/* 525 */ this.parseRes = Boolean.TRUE;
/*     */
                else
/* 527 */           this.parseRes = Boolean.FALSE;
/*     */
            }
/* 529 */
            else if (this.operator == 14) {
/* 530 */
                String pStr = To.objToString(this.expB.parseRes);
/* 531 */
                Pattern p = Pattern.compile(pStr);
/* 532 */
                Matcher m = p.matcher(" " + this.expA.parseRes + " ");
/* 533 */
                if (m.find())
/* 534 */ this.parseRes = Boolean.TRUE;
/*     */
                else
/* 536 */           this.parseRes = Boolean.FALSE;
/*     */
            }
/* 538 */
            else if (this.operator == 13) {
/* 539 */
                if (mCharMatch(To.objToString(this.expA.parseRes), To.objToString(this.expB.parseRes)))
/* 540 */ this.parseRes = Boolean.FALSE;
/*     */
                else
/* 542 */           this.parseRes = Boolean.TRUE;
/*     */
            }
/* 544 */
            else if (this.operator == 15) {
/* 545 */
                String pStr = To.objToString(this.expB.parseRes);
/* 546 */
                Pattern p = Pattern.compile(pStr);
/* 547 */
                Matcher m = p.matcher(" " + this.expA.parseRes + " ");
/* 548 */
                if (m.find())
/* 549 */ this.parseRes = Boolean.FALSE;
/*     */
                else {
/* 551 */
                    this.parseRes = Boolean.TRUE;
/*     */
                }
/*     */
            }
/*     */
        }
/* 555 */
        if (this.parseRes != null) {
/* 556 */
            this.parseResType = 4;
/* 557 */
            this.expA = new MathExpression(this.blkExp, this.parseRes.toString().toUpperCase());
/* 558 */
            this.expA.parse();
/* 559 */
            this.expB = null;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private int getCompareType() {
/* 563 */
        if ((this.expA.resultDataType == -5) ||
/* 564 */       (this.expA.resultDataType == 8) || 
/* 565 */       (this.expB.resultDataType == -5) || 
/* 566 */       (this.expB.resultDataType == 8)) {
/* 567 */
            return 8;
/*     */
        }
/* 569 */
        return 12;
/*     */
    }

    /*     */
/*     */
    public boolean isStaticTrue()
/*     */     throws HgException
/*     */ {
/* 578 */
        if (!this.parsed) parse();
/* 579 */
        return (this.parseResType == 4) && (this.parseRes != null) && (this.parseRes.equals(Boolean.TRUE));
/*     */
    }

    /*     */
/*     */
    public boolean isStaticFalse()
/*     */     throws HgException
/*     */ {
/* 587 */
        if (!this.parsed) parse();
/* 588 */
        return (this.parseResType == 4) && (this.parseRes != null) && (this.parseRes.equals(Boolean.FALSE));
/*     */
    }

    /*     */
/*     */
    public void eval(Conn conn)
/*     */     throws HgException
/*     */ {
/* 594 */
        super.eval(conn);
/* 595 */
        this.expA.data = this.data;
/* 596 */
        this.expA.dataStyle = this.dataStyle;
/* 597 */
        this.expA.eval(conn);
/* 598 */
        if (this.expA.resultDataStyle == 4) {
/* 599 */
            ((MathExpression) this.expA).compress();
/*     */
        }
/* 601 */
        if ((this.operator != 2) && (this.operator != 1))
/*     */ {
/* 603 */
            evalExpB(conn);
/*     */
        }
/* 605 */
        if (this.parseResType == 4) {
/* 606 */
            this.result = new Boolean(To.toBool(this.expA.result));
/* 607 */
        } else if (this.operator == 2) {
/* 608 */
            this.result = Boolean.TRUE;
/* 609 */
            if (!To.toBool(this.expA.result))
/*     */ {
/* 611 */
                this.result = Boolean.FALSE;
/*     */
            } else {
/* 613 */
                evalExpB(conn);
/* 614 */
                if (!To.toBool(this.expB.result))
/*     */ {
/* 616 */
                    this.result = Boolean.FALSE;
/*     */
                }
/*     */
            }
/* 619 */
        } else if (this.operator == 1) {
/* 620 */
            this.result = Boolean.FALSE;
/* 621 */
            if (To.toBool(this.expA.result))
/*     */ {
/* 623 */
                this.result = Boolean.TRUE;
/*     */
            } else {
/* 625 */
                evalExpB(conn);
/* 626 */
                if (To.toBool(this.expB.result))
/*     */ {
/* 628 */
                    this.result = Boolean.TRUE;
/*     */
                }
/*     */
            }
/* 631 */
        } else if (this.operator == 3) {
/* 632 */
            if (To.toBool(this.expA.result))
/* 633 */ this.result = Boolean.FALSE;
/*     */
            else
/* 635 */         this.result = Boolean.TRUE;
/*     */
        }
/* 637 */
        else if (this.operator == 16) {
/* 638 */
            if (((RowSet) this.expA.result).size() > 0)
/* 639 */ this.result = Boolean.TRUE;
/*     */
            else
/* 641 */         this.result = Boolean.FALSE;
/*     */
        }
/*     */
        else
/*     */ {
/* 645 */
            int n = 0;
/* 646 */
            if (this.parseResType == 3) {
/* 647 */
                if (this.ration == 1)
/* 648 */ this.result = Boolean.FALSE;
/* 649 */
                else if (this.ration == 2) {
/* 650 */
                    this.result = Boolean.TRUE;
/*     */
                }
/* 652 */
                RowSet rowSet = (RowSet) this.expB.result;
/* 653 */
                if ((rowSet.fieldSize() > 0) && (rowSet.size() > 0)) {
/* 654 */
                    Field field = rowSet.fieldAt(0);
/*     */
                    int type;
/*     */
/* 655 */
                    if (((this.expA.resultDataType == -5) || (this.expA.resultDataType == 8)) && (
/* 656 */             (field.type == -5) || (field.type == 8)))
/* 657 */ type = 8;
/*     */
                    else {
/* 659 */
                        type = 12;
/*     */
                    }
/*     */ 
/* 662 */
                    for (int i = 0; i < rowSet.size(); i++) {
/* 663 */
                        n = ObjectUtil.compare(this.expA.result, rowSet.getCellValue(i, 0), type);
/* 664 */
                        boolean b = false;
/* 665 */
                        if (((this.operator == 7) && (n >= 0)) ||
/* 666 */               ((this.operator == 6) && (n > 0)) || 
/* 667 */               ((this.operator == 4) && (n == 0)) || 
/* 668 */               ((this.operator == 5) && (n != 0)) || 
/* 669 */               ((this.operator == 9) && (n <= 0)) || (
/* 670 */               (this.operator == 8) && (n < 0))) {
/* 671 */
                            b = true;
/*     */
                        }
/* 673 */
                        if ((this.ration == 1) && (b)) {
/* 674 */
                            this.result = Boolean.TRUE;
/* 675 */
                            break;
/*     */
                        }
/* 677 */
                        if ((this.ration == 2) && (!b)) {
/* 678 */
                            this.result = Boolean.FALSE;
/* 679 */
                            break;
/*     */
                        }
/*     */
                    }
/*     */
                }
/*     */
            } else {
/* 684 */
                int type = getCompareType();
/* 685 */
                if ((this.operator != 10) && (this.operator != 11)) {
/* 686 */
                    n = ObjectUtil.compare(this.expA.result, this.expB.result, type);
/*     */
                }
/*     */
                else {
/* 689 */
                    Field field = ((RowSet) this.expB.result).fieldAt(0);
/* 690 */
                    if (((this.expA.resultDataType == -5) || (this.expA.resultDataType == 8)) && (
/* 691 */             (field.type == -5) || (field.type == 8)))
/* 692 */ type = 8;
/*     */
                    else {
/* 694 */
                        type = 12;
/*     */
                    }
/*     */
                }
/* 697 */
                this.result = Boolean.FALSE;
/* 698 */
                if (((this.operator == 7) && (n >= 0)) ||
/* 699 */           ((this.operator == 6) && (n > 0)) || 
/* 700 */           ((this.operator == 4) && (n == 0)) || 
/* 701 */           ((this.operator == 5) && (n != 0)) || 
/* 702 */           ((this.operator == 9) && (n <= 0)) || (
/* 703 */           (this.operator == 8) && (n < 0))) {
/* 704 */
                    this.result = Boolean.TRUE;
/* 705 */
                } else if (this.operator == 10) {
/* 706 */
                    RowSet rowSet = (RowSet) this.expB.result;
/* 707 */
                    this.result = Boolean.FALSE;
/* 708 */
                    for (int i = 0; i < rowSet.size(); i++)
/* 709 */
                        if (ObjectUtil.compare(rowSet.getCellValue(i, 0), this.expA.result, type) == 0) {
/* 710 */
                            this.result = Boolean.TRUE;
/* 711 */
                            break;
/*     */
                        }
/*     */
                }
/* 714 */
                else if (this.operator == 12)
/*     */ {
/* 716 */
                    if (mCharMatch(To.objToString(this.expA.result),
/* 716 */             To.objToString(this.expB.result)))
/* 717 */ this.result = Boolean.TRUE;
/*     */
                    else
/* 719 */             this.result = Boolean.FALSE;
/*     */
                }
/* 721 */
                else if (this.operator == 14) {
/* 722 */
                    String pStr = To.objToString(this.expB.result);
/* 723 */
                    Pattern p = Pattern.compile(pStr);
/* 724 */
                    Matcher m = p.matcher(" " + this.expA.result + " ");
/* 725 */
                    if (m.find())
/* 726 */ this.result = Boolean.TRUE;
/*     */
                    else
/* 728 */             this.result = Boolean.FALSE;
/*     */
                }
/* 730 */
                else if (this.operator == 11) {
/* 731 */
                    RowSet rowSet = (RowSet) this.expB.result;
/* 732 */
                    this.result = Boolean.TRUE;
/* 733 */
                    for (int i = 0; i < rowSet.size(); i++)
/* 734 */
                        if (ObjectUtil.compare(rowSet.getCellValue(i, 0), this.expA.result, type) == 0) {
/* 735 */
                            this.result = Boolean.FALSE;
/* 736 */
                            break;
/*     */
                        }
/*     */
                }
/* 739 */
                else if (this.operator == 13) {
/* 740 */
                    if (mCharMatch(To.objToString(this.expA.result), To.objToString(this.expB.result)))
/* 741 */ this.result = Boolean.FALSE;
/*     */
                    else
/* 743 */             this.result = Boolean.TRUE;
/*     */
                }
/* 745 */
                else if (this.operator == 15) {
/* 746 */
                    String pStr = To.objToString(this.expB.result);
/* 747 */
                    Pattern p = Pattern.compile(pStr);
/* 748 */
                    Matcher m = p.matcher(" " + this.expA.result + " ");
/* 749 */
                    if (m.find())
/* 750 */ this.result = Boolean.FALSE;
/*     */
                    else
/* 752 */             this.result = Boolean.TRUE;
/*     */
                }
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public boolean isTrue(Conn conn)
/*     */     throws HgException
/*     */ {
/* 762 */
        eval(conn);
/* 763 */
        return To.toBool(this.result);
/*     */
    }

    /*     */
/*     */
    private boolean mCharMatch(String str, String likeStr)
/*     */ {
/* 772 */
        if (likeStr.indexOf('%') < 0)
/*     */ {
/* 774 */
            if (str.length() != likeStr.length()) {
/* 775 */
                return false;
/*     */
            }
/* 777 */
            return sCharMatch(str, likeStr);
/*     */
        }
/*     */ 
/* 780 */
        if ((likeStr.length() > 0) && (likeStr.charAt(0) != '%')) {
/* 781 */
            String tmpStr = likeStr.substring(0, likeStr.indexOf('%'));
/* 782 */
            likeStr = likeStr.substring(likeStr.indexOf('%') + 1);
/* 783 */
            if (str.length() < tmpStr.length()) {
/* 784 */
                return false;
/*     */
            }
/* 786 */
            if (!sCharMatch(str.substring(0, tmpStr.length()), tmpStr)) {
/* 787 */
                return false;
/*     */
            }
/* 789 */
            str = str.substring(tmpStr.length());
/*     */
        }
/*     */ 
/* 792 */
        if ((likeStr.length() > 0) && (likeStr.charAt(likeStr.length() - 1) != '%')) {
/* 793 */
            String tmpStr = likeStr.substring(likeStr.lastIndexOf('%') + 1);
/* 794 */
            likeStr = likeStr.substring(0, likeStr.lastIndexOf('%'));
/* 795 */
            if (str.length() < tmpStr.length()) {
/* 796 */
                return false;
/*     */
            }
/* 798 */
            if (!sCharMatch(str.substring(str.length() - tmpStr.length()), tmpStr)) {
/* 799 */
                return false;
/*     */
            }
/* 801 */
            str = str.substring(0, str.length() - tmpStr.length());
/*     */
        }
/*     */ 
/* 805 */
        String[] likes = likeStr.split("%");
/* 806 */
        int nPos = 0;
/* 807 */
        for (int i = 0; i < likes.length; i++) {
/* 808 */
            if (likes[i].length() > 0) {
/* 809 */
                if (likes[i].indexOf('_') < 0) {
/* 810 */
                    nPos = str.indexOf(likes[i], nPos);
/* 811 */
                    if (nPos < 0) return false;
/* 812 */
                    nPos += likes[i].length();
/*     */
                }
/*     */
                else {
/* 815 */
                    ArrayList strList = new ArrayList();
/* 816 */
                    StringBuffer sb = new StringBuffer();
/* 817 */
                    for (int j = 0; j < likes[i].length(); j++) {
/* 818 */
                        if (likes[i].charAt(j) != '_') {
/* 819 */
                            sb.append(likes[i].charAt(j));
/*     */
                        } else {
/* 821 */
                            if (sb.length() > 0) {
/* 822 */
                                strList.add(sb.toString());
/* 823 */
                                sb.setLength(0);
/*     */
                            }
/* 825 */
                            strList.add(null);
/*     */
                        }
/*     */
                    }
/* 828 */
                    if (sb.length() > 0) {
/* 829 */
                        strList.add(sb.toString());
/*     */
                    }
/* 831 */
                    for (int j = 0; j < strList.size(); j++) {
/* 832 */
                        if (strList.get(j) != null) {
/* 833 */
                            nPos = str.indexOf((String) strList.get(j), nPos);
/* 834 */
                            if (nPos < 0) return false;
/* 835 */
                            nPos += ((String) strList.get(j)).length();
/*     */
                        } else {
/* 837 */
                            nPos++;
/*     */
                        }
/*     */
                    }
/*     */
                }
/*     */
            }
/*     */
        }
/* 843 */
        return true;
/*     */
    }

    /*     */
/*     */
    private boolean sCharMatch(String str, String likeStr)
/*     */ {
/* 852 */
        if (str.equals(likeStr))
/* 853 */ return true;
/* 854 */
        if (str.length() == likeStr.length()) {
/* 855 */
            for (int i = 0; i < str.length(); i++) {
/* 856 */
                if ((likeStr.charAt(i) != '_') && (str.charAt(i) != likeStr.charAt(i))) {
/* 857 */
                    return false;
/*     */
                }
/*     */
            }
/* 860 */
            return true;
/*     */
        }
/* 862 */
        return false;
/*     */
    }

    /*     */
/*     */
    public ArrayList getChildExpList(Conn conn) throws HgException {
/* 866 */
        if (!this.parsed) parse();
/* 867 */
        ArrayList list = new ArrayList();
/* 868 */
        if (this.expA != null) list.add(this.expA);
/* 869 */
        if (this.expB != null) list.add(this.expB);
/* 870 */
        return list;
/*     */
    }

    /*     */
    public void fillVarList(ArrayList list) throws HgException {
/* 873 */
        if (!this.parsed) parse();
/* 874 */
        if (this.expA != null) this.expA.fillVarList(list);
/* 875 */
        if (this.expB != null) this.expB.fillVarList(list);
/*     */
    }
/*     */
}

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.LogicExpression
 * JD-Core Version:    0.6.0
 */